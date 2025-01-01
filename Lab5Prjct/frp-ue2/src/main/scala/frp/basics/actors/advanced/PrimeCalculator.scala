package frp.basics.actors.advanced

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors, TimerScheduler}
import akka.actor.typed.{ActorRef, Behavior}
import frp.basics.actors.RangeUtil
import frp.basics.actors.advanced.PrimeCalculator.WrappedPrimeFinderReply
import frp.basics.actors.advanced.PrimeFinder.PartialResult
import  scala.concurrent.duration.DurationInt
import scala.collection.mutable



object PrimeCalculator:
  sealed trait Command
  private final case class WrappedPrimeFinderReply(reply: PrimeFinder.Reply) extends Command

  private case object Resend extends Command

  sealed trait Reply
  final case class Result(lower: Int, upper: Int, primes: Seq[Int]) extends Reply
  final case class Failed(lower: Int, upper: Int, reason: String) extends Reply


  def apply(lower: Int, upper: Int,
            replyTo: ActorRef[Reply],
            workPool: ActorRef[PrimeFinder.Command]): Behavior[Command] =
    Behaviors.setup { context =>
      Behaviors.withTimers { timer =>
        new PrimeCalculator(context, lower, upper, replyTo, workPool, timer)
      }
    }
  end apply

end PrimeCalculator

class PrimeCalculator(context: ActorContext[PrimeCalculator.Command],
                      lower: Int, upper: Int,
                      replyTo: ActorRef[PrimeCalculator.Reply],
                      workPool: ActorRef[PrimeFinder.Command],
                      timer: TimerScheduler[PrimeCalculator.Command])
  extends AbstractBehavior[PrimeCalculator.Command](context):

  val MAX_WORKERS = Runtime.getRuntime.availableProcessors()
  val RESEND_INTERVAL = 50.millis
  val primes = mutable.SortedSet.empty[Int]
  //var numUnfishinedTasks = 0
  val unfinishedTasks = mutable.Set.empty[(Int, Int)]
  val messageAdapter = context.messageAdapter(reply => WrappedPrimeFinderReply(reply))

  // primary constructor statements
  createTasks
  sendUnfinishedTasks

  timer.startTimerAtFixedRate(PrimeCalculator.Resend, RESEND_INTERVAL)

  private def createTasks: Unit =
    val intervals = RangeUtil.splitIntoIntervals(lower, upper, MAX_WORKERS)
    for((l, u) <- intervals)
      unfinishedTasks += ((l, u))
  end createTasks

  private def sendUnfinishedTasks: Unit =
    for((l, u) <- unfinishedTasks)
      workPool ! PrimeFinder.Find(l, u, messageAdapter)
    end for
  end sendUnfinishedTasks

  override def onMessage(msg: PrimeCalculator.Command): Behavior[PrimeCalculator.Command] =
    msg match
      case WrappedPrimeFinderReply(PartialResult(l, u, p)) =>
        primes ++= p
        unfinishedTasks -= ((l, u))
        if unfinishedTasks.isEmpty then
          replyTo ! PrimeCalculator.Result(lower, upper, primes.toSeq)
          Behaviors.stopped
        else
          Behaviors.same
      case PrimeCalculator.Resend =>
        sendUnfinishedTasks
        Behaviors.same

end PrimeCalculator

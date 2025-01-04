package frp.basics.actors.task2

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.pattern.pipe
import akka.actor.typed.scaladsl.adapter._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object PrimeChecker2 {
  sealed trait Command
  final case class CheckPrime(number: Int, replyTo: ActorRef[PrimeCheckerClient2.Command]) extends Command
  final case class FactorizationComplete(number: Int, factors: Seq[Int], replyTo: ActorRef[PrimeCheckerClient2.Command]) extends Command
  case object Shutdown extends Command

  def apply(): Behavior[Command] =
    Behaviors.setup(context => new PrimeChecker2(context))

  def isPrime(n: Int): Boolean = {
    if (n <= 1) false
    else if (n == 2) true
    else if (n % 2 == 0) false
    else {
      val limit = Math.sqrt(n).toInt
      (3 to limit by 2).forall(n % _ != 0)
    }
  }

  def factor(n: Int): Future[Seq[Int]] = Future {
    require(n > 0, "Number must be greater than 0")
    Thread.sleep(if (n % 3 == 0) 2000 else 100) // Simulate longer computation for some numbers
    (1 to n).filter(n % _ == 0)
  }
}

class PrimeChecker2(context: ActorContext[PrimeChecker2.Command])
  extends AbstractBehavior[PrimeChecker2.Command](context) {

  import PrimeChecker2._
  

  override def onMessage(msg: Command): Behavior[Command] = {
    msg match {
      case CheckPrime(number, replyTo) =>
        val isPrimeResult = isPrime(number)
        factor(number)
          .map(factors => FactorizationComplete(number, factors, replyTo))
          .pipeTo(context.self.toClassic)
        Behaviors.same

      case FactorizationComplete(number, factors, replyTo) =>
        replyTo ! PrimeCheckerClient2.PrimeResult(number, factors, isPrime(number))
        Behaviors.same

      case Shutdown =>
        Behaviors.stopped
    }
  }
}

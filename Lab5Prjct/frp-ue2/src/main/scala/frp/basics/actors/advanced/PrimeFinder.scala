package frp.basics.actors.advanced

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior, PostStop, PreRestart}
import frp.basics.actors.PrimeUtil.isPrime

import scala.util.Random

object PrimeFinder:

  sealed trait Command
  final case class Find(lower: Int, upper: Int, replyTo: ActorRef[Reply]) extends Command

  sealed trait Reply
  final case class PartialResult(lower: Int, upper: Int, primes: Seq[Int]) extends Reply

  def failSometimes(probability: Double, actorName: String, l: Int, u: Int): Unit =
    if Random.nextDouble() < probability then
      println(s"  $actorName:  FAILED [$l, $u]")
      throw ArithmeticException("Prime computation failed for some reason")
  end failSometimes

  def apply(): Behavior[Command] =
    Behaviors
      .receive[Command] {
        case (context, Find(lower, upper, replyTo)) =>
          println(s"  ${context.self.path.name} (Thread=${Thread.currentThread.threadId}) START [$lower, $upper]")
          val primes = (lower to upper) filter isPrime
          failSometimes(0.4, context.self.path.name, lower, upper)
          println(s"  ${context.self.path.name} (Thread=${Thread.currentThread.threadId}) SUCCEEDED [$lower, $upper]")
          replyTo ! PartialResult(lower, upper, primes)
          Behaviors.same
      }
      .receiveSignal {
        case (context, PostStop) =>
          println(s" ${context.self.path.name} STOPPED")
          Behaviors.same
        case (context, PreRestart) =>
          println(s" ${context.self.path.name} RESTARTED")
          Behaviors.same
      }
  end apply
end PrimeFinder
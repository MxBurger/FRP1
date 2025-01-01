package frp.basics.actors.simple

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior, javadsl}
import frp.basics.actors.PrimeUtil.isPrime

object SimplePrimeCalculator:
  sealed trait Command
  final case class Find(lower: Int, upper: Int, replyTo: ActorRef[Reply]) extends Command
  case object Shutdown extends Command

  sealed trait Reply
  final case class Found(lower: Int, upper: Int, primes: Seq[Int]) extends Reply
  
  def ooBehavior(): Behavior[Command] =
    Behaviors.setup { context =>
      new SimplePrimeCalculator(context)
    }

  def functionalBehavior(): Behavior[Command] =
    Behaviors.receiveMessage {
      case Find(l, u, replyto) =>
        val primes = (l to u) filter (n => isPrime(n))
        replyto ! Found(l, u, primes) // send response
        Behaviors.same // stay in the same behavior
      case Shutdown => Behaviors.stopped // stop the actor
    }

  def apply(): Behavior[Command] =
    Behaviors.setup(ctx => new SimplePrimeCalculator(ctx))  // Factory method

end SimplePrimeCalculator

class SimplePrimeCalculator(context: ActorContext[SimplePrimeCalculator.Command])  // can be implemented using class or function (like React)
  extends AbstractBehavior[SimplePrimeCalculator.Command](context):

  import SimplePrimeCalculator._  // Import necessary
  override def onMessage(msg: SimplePrimeCalculator.Command): Behavior[SimplePrimeCalculator.Command] =
    msg match {
      case Find(l, u, replyto) =>
        val primes = (l to u) filter (n => isPrime(n))
        replyto ! Found(l, u, primes) // send response
        Behaviors.same // stay in the same behavior
      case Shutdown => Behaviors.stopped // stop the actor
    }

end SimplePrimeCalculator

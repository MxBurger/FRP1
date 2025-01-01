package frp.basics.actors.simple

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}
import frp.basics.actors.PrimeUtil.isPrime

object SimplePrimeCalculator:
  sealed trait Command
  final case class Find(lower: Int, upper: Int, replyTo: ActorRef[Reply]) extends Command

  sealed trait Reply
  final case class Found(lower: Int, upper: Int, primes Seq[Int]) extends Reply

  def apply(): Behavior[Command] =
    Behavior.setup(context => new SimplePrimeCalculator(context))

end SimplePrimeCalculator

class SimplePrimeCalculator(context: ActorContext[SimplePrimeCalculator.Command])
  extends AbstractBehavior[SimplePrimeCalculator.Command](context):

  import SimplePrimeCalculator._

  override def onMessage(msg: Command): Behavior[Command] =
    msg match
      case Find(l, u,  replyTo) =>
        val primes = (l to u).filter(n => isPrime(n))
        replyTo ! Found(l, u, primes)
        Behaviors.same // behavior does not change after processing the message

end SimplePrimeCalculator

package frp.basics.actors.simple

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior, Signal, Terminated}

object SimpleMainActor:
  def apply(): Behavior[SimplePrimeCalculator.Reply] = 
    Behaviors.setup(context => new SimpleMainActor(context))

end SimpleMainActor

class SimpleMainActor(context: ActorContext[SimplePrimeCalculator.Reply])
  extends AbstractBehavior[SimplePrimeCalculator.Reply](context):

  import SimplePrimeCalculator._

  var calc: ActorRef[Command] = context.spawn(SimplePrimeCalculator(), "simple-prime-calculator")
  calc ! Find(1, 100, context.self)

  override def onMessage(msg: Reply): Behavior[Reply] =
    msg match
      case Found(l, u, primes) =>
        println(s"Primes between $l and $u: ${primes.mkString(", ")}")
        Behaviors.same
end SimpleMainActor
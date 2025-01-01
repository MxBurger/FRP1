package frp.basics.actors.simple

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior, Signal, Terminated}

object SimpleMainActor:

  def apply(): Behavior[SimplePrimeCalculator.Reply] =
    Behaviors.setup(context => new SimpleMainActor(context))

end SimpleMainActor

class SimpleMainActor(ctx: ActorContext[SimplePrimeCalculator.Reply])
  extends AbstractBehavior[SimplePrimeCalculator.Reply](ctx):

  import SimplePrimeCalculator._

  var calc: ActorRef[Command] = context.spawn(SimplePrimeCalculator.functionalBehavior(), "simple-prime-calculator")
  calc ! Find(2, 100, context.self) // context.self is a Reference to the own context
  calc ! Find(10, 2000, context.self)
  calc ! Shutdown

  context.watch(calc)

  override def onMessage(msg: SimplePrimeCalculator.Reply): Behavior[SimplePrimeCalculator.Reply] =
    msg match {
      case Found(l, u, primes) =>
        println(s"primes in [$l, $u]: [${primes.mkString(",")}]")
        Behaviors.same
    }

  override def onSignal: PartialFunction[Signal, Behavior[Reply]] = {
    case Terminated(_) => // we could differentiate between different actors
      println("Calculator terminated")
      context.system.terminate()
      Behaviors.stopped
  }
end SimpleMainActor
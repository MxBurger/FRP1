package frp.basics.actors.advanced

import scala.concurrent.duration.DurationInt
import akka.actor.typed.scaladsl.{ActorContext, Behaviors, PoolRouter, Routers}
import akka.actor.typed.{ActorRef, Behavior, SupervisorStrategy, Terminated}

object MainActor:

  private def workPoolBehavior(): Behavior[PrimeFinder.Command] =
    Routers.pool(4){
      Behaviors.supervise(PrimeFinder())
        .onFailure[Exception](SupervisorStrategy.restart)
    }


  def apply(): Behavior[PrimeCalculator.Reply] =
    Behaviors.setup { context =>

      var workPool = context.spawn(workPoolBehavior(), "worker-pool")

      context.spawn(PrimeCalculator(2, 200, context.self, workPool), "prime-calculator-1")
      context.spawn(PrimeCalculator(1000, 1200, context.self, workPool), "prime-calculator-2")

      context.children.foreach(context.watch)


      Behaviors.receiveMessage[PrimeCalculator.Reply] {
        case PrimeCalculator.Result(lower, upper, primes) =>
          println(s"prime calculation in interval [$lower, $upper] SUCCEDED: [${primes.mkString(", ")}]")
          Behaviors.same
        case PrimeCalculator.Failed(lower, upper, reason) =>
          println(s"prime calculation in interval [$lower, $upper] FAILED: $reason")
          Behaviors.same
      }
        .receiveSignal {
          case (context, Terminated(_)) =>
            if context.children.size <= 1 then
              context.system.terminate()
              Behaviors.stopped
            else
            Behaviors.same
        }

    }
  end apply

end MainActor

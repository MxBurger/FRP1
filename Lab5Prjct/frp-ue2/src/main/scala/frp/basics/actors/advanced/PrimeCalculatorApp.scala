package frp.basics.actors.advanced

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors

import scala.concurrent.Await
import scala.concurrent.duration.Duration

@main
def primeCalculatorMain(): Unit =

  println("==================== PrimeCalculatorApp ==========================")

  val system = ActorSystem(MainActor(), "prime-calculator-system")

  Await.ready(system.whenTerminated, Duration.Inf)
end primeCalculatorMain

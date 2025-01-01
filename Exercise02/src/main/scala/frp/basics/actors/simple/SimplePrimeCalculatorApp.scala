package frp.basics.actors.simple

import akka.actor.typed.ActorSystem
import scala.concurrent.Await
import scala.concurrent.duration.Duration

@main
def simplePrimeCalculatorMain(): Unit =

  println("==================== SimplePrimeCalculatorApp ==========================")

  ActorSystem(SimpleMainActor(), "simple-prime-calculator-system")

  Thread.sleep(3000)
  system.terminate()
  Await.result(system.whenTerminated, Duration.Inf)

end simplePrimeCalculatorMain
package frp.basics.actors.simple

import akka.actor.typed.ActorSystem
import scala.concurrent.Await
import scala.concurrent.duration.Duration

@main
def simplePrimeCalculatorMain(): Unit =

  println("==================== SimplePrimeCalculatorApp ==========================")

  var system = ActorSystem(SimpleMainActor.apply(), "simple-prime-calculator-system")

  //system.terminate()  // stop actor system --> only necessary, if the system should terminate

  Await.ready(system.whenTerminated, Duration.Inf)

end simplePrimeCalculatorMain
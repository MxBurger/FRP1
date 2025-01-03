package frp.basics.actors.task2

@main
def task2Main1(): Unit =
  println("==================== Task2Main1 ==========================")
  
  val system = akka.actor.typed.ActorSystem(PrimeChecker1(), "prime-checker-system")
  
  val primeCheckerClient = system.systemActorOf(
    PrimeCheckerClient1(system),
    "prime-checker-client"
  )
  
  Thread.sleep(5000)
  
  system.terminate()
end task2Main1
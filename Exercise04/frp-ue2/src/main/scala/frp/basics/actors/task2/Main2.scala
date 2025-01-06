package frp.basics.actors.task2

@main
def task2Main2(): Unit =
  println("==================== Task2Main2 ==========================")

  val system = akka.actor.typed.ActorSystem(PrimeChecker2(), "prime-checker-system")

  val client = system.systemActorOf(
    PrimeCheckerClient2(system),
    "prime-checker-client"
  )
  
  Thread.sleep(15000) 

  system.terminate()
end task2Main2
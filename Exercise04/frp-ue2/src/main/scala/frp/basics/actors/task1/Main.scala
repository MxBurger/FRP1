package frp.basics.actors.task1

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior, Signal, Terminated}
import frp.basics.actors.task1.MessageSender.Command
import scala.util.Random

@main
def task1Main(): Unit =
  println("==================== Task1Main ==========================")
  
  val system = akka.actor.typed.ActorSystem(MessageReceiver(), "message-receiver-system")
  
  val messageSender: ActorRef[MessageSender.Command] = system.systemActorOf(
    MessageSender(maxRetries = 2),
    "message-sender"
  )
  
  val messageReceiver: ActorRef[MessageReceiver.Command] = system.systemActorOf(
    MessageReceiver(),
    "message-receiver"
  )
  
  messageSender ! MessageSender.SendMessage(Message(1, "Hello"), messageReceiver)
  Thread.sleep(500)
  messageSender ! MessageSender.SendMessage(Message(2, "How are you?"), messageReceiver)
  Thread.sleep(500)
  messageSender ! MessageSender.SendMessage(Message(3, "Goodbye"), messageReceiver)
  
  Thread.sleep(5000)
  
  messageSender ! MessageSender.Shutdown
  messageReceiver ! MessageReceiver.Shutdown
  system.terminate()
end task1Main
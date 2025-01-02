package frp.basics.actors.task1

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior, Signal, Terminated}
import frp.basics.actors.task1.MessageSender.Command

@main
def task1Main(): Unit =
  println("==================== Task1Main ==========================")

  // Create an actor system
  val system = akka.actor.typed.ActorSystem(MessageReceiver(), "message-receiver-system")

  // Create sender actor
  val messageSender: ActorRef[MessageSender.Command] = system.systemActorOf(MessageSender(), "message-sender")

  // Create receiver actor
  val messageReceiver: ActorRef[MessageReceiver.Command] = system.systemActorOf(MessageReceiver(), "message-receiver")

  // Send some messages to the receiver
  messageSender ! MessageSender.SendMessage(Message(1, "Hello"), messageReceiver)
  messageSender ! MessageSender.SendMessage(Message(2, "How are you?"), messageReceiver)
  messageSender ! MessageSender.SendMessage(Message(3, "Goodbye"), messageReceiver)

  // Wait a bit to allow message processing
  Thread.sleep(1000)

  // Shutdown the actors
  messageSender ! MessageSender.Shutdown
  messageReceiver ! MessageReceiver.Shutdown

  // Shutdown the actor system
  system.terminate()
end task1Main
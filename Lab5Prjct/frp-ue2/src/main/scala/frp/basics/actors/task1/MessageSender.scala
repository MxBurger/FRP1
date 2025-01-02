package frp.basics.actors.task1

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.ActorRef
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object MessageSender {
  sealed trait Command
  final case class SendMessage(msg: Message, replyTo: ActorRef[MessageReceiver.Command]) extends Command
  case object Shutdown extends Command

  def apply(): Behavior[Command] =
    Behaviors.setup(ctx => new MessageSender(ctx)) // Factory method
}

class MessageSender(context: ActorContext[MessageSender.Command]) extends AbstractBehavior[MessageSender.Command](context):
  import MessageSender._

  private val timestampFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

  override def onMessage(msg: MessageSender.Command): Behavior[MessageSender.Command] =
    msg match {
      case SendMessage(msg, actorToSend) =>
        val timestamp = LocalDateTime.now().format(timestampFormat)
        println(s"[$timestamp] Sending message - ID: ${msg.id}, Text: ${msg.text}")
        actorToSend ! MessageReceiver.ReceiveMessage(msg)
        Behaviors.same
      case Shutdown =>
        println("Shutting down sender")
        Behaviors.stopped
    }
end MessageSender
package frp.basics.actors.task1

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object MessageReceiver {
  sealed trait Command
  final case class ReceiveMessage(msg: Message) extends Command
  case object Shutdown extends Command

  def apply(): Behavior[Command] =
    Behaviors.setup(ctx => new MessageReceiver(ctx))  // Factory method
}

class MessageReceiver(context: ActorContext[MessageReceiver.Command])
  extends AbstractBehavior[MessageReceiver.Command](context):

  private val timestampFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

  override def onMessage(msg: MessageReceiver.Command): Behavior[MessageReceiver.Command] =
    msg match {
      case MessageReceiver.ReceiveMessage(msg) =>
        val timestamp = LocalDateTime.now().format(timestampFormat)
        println(s"[$timestamp] Message received - ID: ${msg.id}, Text: ${msg.text}")
        Behaviors.same
      case MessageReceiver.Shutdown =>
        println("Shutting down receiver")
        Behaviors.stopped
    }
end MessageReceiver
package frp.basics.actors.task1

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.util.Random

object MessageReceiver {
  sealed trait Command
  final case class ReceiveMessage(msg: Message, replyTo: akka.actor.typed.ActorRef[MessageSender.Command]) extends Command
  case object Shutdown extends Command

  def apply(): Behavior[Command] =
    Behaviors.setup(ctx => new MessageReceiver(ctx))
}

class MessageReceiver(context: ActorContext[MessageReceiver.Command])
  extends AbstractBehavior[MessageReceiver.Command](context):

  private val timestampFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
  private val random = new Random()
  private var processedMessages = Set[Int]()

  override def onMessage(msg: MessageReceiver.Command): Behavior[MessageReceiver.Command] =
    msg match {
      case MessageReceiver.ReceiveMessage(msg, replyTo) =>
        val timestamp = LocalDateTime.now().format(timestampFormat)

        // Simulate message loss (30% chance)
        if (random.nextDouble() < 0.3) {
          println(s"[$timestamp] Message lost (simulated) - ID: ${msg.id}")
          Behaviors.same
        } else {
          // Check if message was already processed
          if (!processedMessages.contains(msg.id)) {
            println(s"[$timestamp] Message received - ID: ${msg.id}, Text: ${msg.text}")
            processedMessages += msg.id

            // Simulate confirmation loss (30% chance)
            if (random.nextDouble() < 0.3) {
              println(s"[$timestamp] Confirmation lost (simulated) - for message ID: ${msg.id}")
            } else {
              replyTo ! MessageSender.MessageConfirmed(Confirmation(msg.id))
            }
          } else {
            println(s"[$timestamp] Duplicate message received - ID: ${msg.id}")
            replyTo ! MessageSender.MessageConfirmed(Confirmation(msg.id))
          }
          Behaviors.same
        }

      case MessageReceiver.Shutdown =>
        println("Shutting down receiver")
        Behaviors.stopped
    }
end MessageReceiver
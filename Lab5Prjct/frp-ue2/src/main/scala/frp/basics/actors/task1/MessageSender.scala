package frp.basics.actors.task1

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.ActorRef
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.concurrent.duration._
import scala.collection.mutable

object MessageSender {
  sealed trait Command
  final case class SendMessage(msg: Message, receiver: ActorRef[MessageReceiver.Command]) extends Command
  final case class MessageConfirmed(confirmation: Confirmation) extends Command
  final case class RetryMessage(msgId: Int) extends Command
  case object Shutdown extends Command

  def apply(): Behavior[Command] =
    Behaviors.setup(ctx => new MessageSender(ctx))
}

class MessageSender(context: ActorContext[MessageSender.Command]) extends AbstractBehavior[MessageSender.Command](context):
  import MessageSender._
  import context.executionContext

  private val timestampFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
  private val pendingMessages = mutable.Map[Int, (Message, ActorRef[MessageReceiver.Command], Int)]() // messageId -> (message, receiver, retryCount)
  private val maxRetries = 3
  private val retryDelay = 1.seconds

  override def onMessage(msg: MessageSender.Command): Behavior[MessageSender.Command] =
    msg match {
      case SendMessage(message, receiver) =>
        val timestamp = LocalDateTime.now().format(timestampFormat)
        println(s"[$timestamp] Sending message - ID: ${message.id}, Text: ${message.text}")

        // Store message for potential retry
        pendingMessages(message.id) = (message, receiver, 0)

        // Schedule retry if no confirmation received
        context.scheduleOnce(retryDelay, context.self, RetryMessage(message.id))

        receiver ! MessageReceiver.ReceiveMessage(message, context.self)
        Behaviors.same

      case MessageConfirmed(confirmation) =>
        val timestamp = LocalDateTime.now().format(timestampFormat)
        println(s"[$timestamp] Received confirmation for message ID: ${confirmation.id}")
        pendingMessages.remove(confirmation.id)
        Behaviors.same

      case RetryMessage(msgId) =>
        pendingMessages.get(msgId) match {
          case Some((message, receiver, retryCount)) if retryCount < maxRetries =>
            val timestamp = LocalDateTime.now().format(timestampFormat)
            println(s"[$timestamp] Retrying message - ID: ${message.id}, Attempt: ${retryCount + 1}")

            // Update retry count
            pendingMessages(msgId) = (message, receiver, retryCount + 1)

            // Schedule next retry
            context.scheduleOnce(retryDelay, context.self, RetryMessage(msgId))

            // Resend message
            receiver ! MessageReceiver.ReceiveMessage(message, context.self)

          case Some((message, _, retryCount)) =>
            val timestamp = LocalDateTime.now().format(timestampFormat)
            println(s"[$timestamp] Max retries reached for message ID: ${message.id}")
            pendingMessages.remove(msgId)

          case None => // Message was confirmed, nothing to do
        }
        Behaviors.same

      case Shutdown =>
        println("Shutting down sender")
        Behaviors.stopped
    }
end MessageSender
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

  def apply(maxRetries: Int = 3): Behavior[Command] =
    Behaviors.setup(ctx => new MessageSender(ctx, maxRetries))
}

class MessageSender(
                     context: ActorContext[MessageSender.Command],
                     maxRetries: Int
                   ) extends AbstractBehavior[MessageSender.Command](context):
  
  import MessageSender._

  private val timestampFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
  private val pendingMessages = mutable.Map[Int, (Message, ActorRef[MessageReceiver.Command], Int)]()
  private val retryDelay = 1.second

  override def onMessage(msg: MessageSender.Command): Behavior[MessageSender.Command] =
    msg match {
      case SendMessage(message, receiver) =>
        val timestamp = LocalDateTime.now().format(timestampFormat)
        println(s"[$timestamp] Sending message - ID: ${message.id}, Text: ${message.text} (max retries: $maxRetries)")

        pendingMessages(message.id) = (message, receiver, 0)
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
            val nextRetry = retryCount + 1
            println(s"[$timestamp] Retrying message - ID: ${message.id}, Attempt: $nextRetry/$maxRetries")

            pendingMessages(msgId) = (message, receiver, nextRetry)
            context.scheduleOnce(retryDelay, context.self, RetryMessage(msgId))
            receiver ! MessageReceiver.ReceiveMessage(message, context.self)

          case Some((message, _, retryCount)) =>
            val timestamp = LocalDateTime.now().format(timestampFormat)
            println(s"[$timestamp] Max retries ($maxRetries) reached for message ID: ${message.id}")
            pendingMessages.remove(msgId)

          case None => // Message was confirmed, nothing to do
        }
        Behaviors.same

      case Shutdown =>
        println("Shutting down sender")
        Behaviors.stopped
    }
end MessageSender
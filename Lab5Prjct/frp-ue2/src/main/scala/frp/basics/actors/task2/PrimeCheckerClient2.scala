package frp.basics.actors.task2

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.util.Timeout
import scala.concurrent.duration._
import akka.actor.typed.scaladsl.AskPattern._
import scala.util.{Success, Failure}

object PrimeCheckerClient2 {
  sealed trait Command
  final case class PrimeResult(number: Int, factors: Seq[Int], isPrime: Boolean) extends Command
  private case object GenerateNext extends Command
  private case class OperationFailed(number: Int, reason: String) extends Command

  def apply(checker: ActorRef[PrimeChecker2.Command]): Behavior[Command] =
    Behaviors.setup(ctx => new PrimeCheckerClient2(ctx, checker))
}

class PrimeCheckerClient2(
                           context: ActorContext[PrimeCheckerClient2.Command],
                           checker: ActorRef[PrimeChecker2.Command]
                         ) extends AbstractBehavior[PrimeCheckerClient2.Command](context) {

  import PrimeCheckerClient2._

  private var currentNumber = 1
  private val maxNumber = 10

  implicit val timeout: Timeout = 1.seconds
  
  // Schedule the first number generation
  context.self ! GenerateNext

  override def onMessage(msg: Command): Behavior[Command] = {
    msg match {
      case PrimeResult(number, factors, isPrime) =>
        val primeStr = if (isPrime) "prime" else "not prime"
        println(s"$number is $primeStr. Factors: [${factors.mkString(",")}]")
        context.self ! GenerateNext
        Behaviors.same

      case OperationFailed(number, reason) =>
        println(s"Operation failed for number $number: $reason")
        context.self ! GenerateNext
        Behaviors.same

      case GenerateNext =>
        if (currentNumber <= maxNumber) {
          // Using the ask pattern
          context.ask[PrimeChecker2.Command, PrimeCheckerClient2.Command](
            checker,
            ref => PrimeChecker2.CheckPrime(currentNumber, ref)
          ) {
            case Success(response) => response
            case Failure(ex) => OperationFailed(currentNumber, ex.getMessage)
          }

          currentNumber += 1
          Behaviors.same
        } else {
          checker ! PrimeChecker2.Shutdown
          Behaviors.stopped
        }
    }
  }
}

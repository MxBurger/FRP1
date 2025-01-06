package frp.basics.actors.task2

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.pattern.pipe
import akka.actor.typed.scaladsl.adapter._
import scala.concurrent.{ExecutionContext, Future}

object PrimeChecker1 {
  sealed trait Command
  final case class CheckPrime(number: Int, replyTo: ActorRef[PrimeCheckerClient1.Command]) extends Command
  final case class FactorizationComplete(number: Int, factors: Seq[Int], replyTo: ActorRef[PrimeCheckerClient1.Command]) extends Command
  case object Shutdown extends Command

  def apply(): Behavior[Command] =
    Behaviors.setup(context => new PrimeChecker1(context))

  def isPrime(n: Int): Boolean = {
    if (n <= 1) false
    else if (n == 2) true
    else if (n % 2 == 0) false
    else {
      val limit = Math.sqrt(n).toInt
      (3 to limit by 2).forall(n % _ != 0)
    }
  }

  def factor(n: Int)(implicit ec: ExecutionContext): Future[Seq[Int]] = Future {
    require(n > 0, "Number must be greater than 0")
    (1 to n).filter(n % _ == 0)
  }
}

class PrimeChecker1(context: ActorContext[PrimeChecker1.Command])
  extends AbstractBehavior[PrimeChecker1.Command](context) {

  import PrimeChecker1._

  implicit val ec: ExecutionContext = context.executionContext

  override def onMessage(msg: Command): Behavior[Command] = {
    msg match {
      case CheckPrime(number, replyTo) =>
        val isPrimeResult = isPrime(number)
        // Using pipe pattern to handle the Future result
        factor(number)
          .map(factors => FactorizationComplete(number, factors, replyTo))
          .pipeTo(context.self.toClassic) // toClassic is used to convert the typed ActorRef
        Behaviors.same

      case FactorizationComplete(number, factors, replyTo) =>
        replyTo ! PrimeCheckerClient1.PrimeResult(number, factors, isPrime(number))
        Behaviors.same

      case Shutdown =>
        Behaviors.stopped
    }
  }
}

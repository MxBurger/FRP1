package frp.basics.actors.task2

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}

object PrimeCheckerClient1 {
  sealed trait Command
  final case class PrimeResult(number: Int, factors: Seq[Int], isPrime: Boolean) extends Command
  private case object GenerateNext extends Command

  def apply(checker: ActorRef[PrimeChecker1.Command]): Behavior[Command] =
    Behaviors.setup(ctx => new PrimeCheckerClient1(ctx, checker))
}

class PrimeCheckerClient1(
                           context: ActorContext[PrimeCheckerClient1.Command],
                           checker: ActorRef[PrimeChecker1.Command]
                         ) extends AbstractBehavior[PrimeCheckerClient1.Command](context) {

  import PrimeCheckerClient1._

  private var currentNumber = 1
  private val maxNumber = 20

  // Schedule the first number generation
  context.self ! GenerateNext

  override def onMessage(msg: Command): Behavior[Command] = {
    msg match {
      case PrimeResult(number, factors, isPrime) =>
        val primeStr = if (isPrime) "prime" else "not prime"
        println(s"$number is $primeStr. Factors: [${factors.mkString(",")}]")
        context.self ! GenerateNext
        Behaviors.same

      case GenerateNext =>
        if (currentNumber <= maxNumber) {
          checker ! PrimeChecker1.CheckPrime(currentNumber, context.self)
          currentNumber += 1
          Behaviors.same
        } else {
          checker ! PrimeChecker1.Shutdown
          Behaviors.stopped
        }
    }
  }
}
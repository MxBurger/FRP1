import Adder.Response
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Adder {
  sealed trait Response

  final case class CalcResult(s: Seq[Int], sum: Int) extends Response

  sealed trait Request

  def apply(s: Seq[Int], replyTo: ActorRef[Response]): Behavior[Nothing] =
    Behaviors.setup { context =>
      val sum = s.sum
      replyTo ! CalcResult(s, sum)
      Behaviors.stopped
    }
}


object Calculator {
  def apply(): Behavior[Response] =
    Behaviors.setup { context =>
      val act1 = context.spawn(Adder(List(1, 2, 3), context.self), "adder-1")
      val act2 = context.spawn(Adder(List(4, 5, 6), context.self), "adder-2")

      context.children.foreach(context.watch)

      def mainBehavior(alive: Int): Behavior[Adder.Response] =
        Behaviors.receiveMessage[Adder.Response] {
            case Adder.CalcResult(s, sum) =>
              println(s"sum of $s: $sum")
              Behaviors.same
          }
          .receiveSignal {
            // Deathwatch signal
            case (_) =>
              if (alive == 1) {
                context.system.terminate()
                Behaviors.stopped
              } else mainBehavior(alive - 1)
          }

      mainBehavior(context.children.count(_ => true))
    }
}

@main
def wow = {
  val system = ActorSystem[Adder.Response](Calculator(), "wow")
  Await.ready(system.whenTerminated, Duration.Inf)
}
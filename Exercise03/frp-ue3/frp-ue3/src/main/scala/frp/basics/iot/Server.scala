package frp.basics.iot

import akka.actor.typed.ActorSystem
import akka.{Done, NotUsed}
import akka.stream.scaladsl.{Flow, Sink, Source}
import frp.basics.DefaultActorSystem
import frp.basics.LogUtil.tracef

import java.time.LocalDateTime
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.util.Random

trait Server:
  def handleMessages(service: Flow[String, String, NotUsed]): Future[Done]
end Server

object ServerSimulator:
  def apply(nrMessages: Int = Int.MaxValue, messagesPerSecond: Int = 20) =
    new ServerSimulator(nrMessages, messagesPerSecond)
end ServerSimulator


class ServerSimulator(nrMessages: Int, messagesPerSecond: Int) extends Server:
  given ActorSystem[Nothing] = DefaultActorSystem()
  private var tracingEnabled = false;

  override def handleMessages(service: Flow[String, String, NotUsed]): Future[Done] =
    val source =
      Source(1 to nrMessages)
        .throttle(messagesPerSecond, 1.second)
        .map(id => Measurement(id, Random.nextDouble() * 100, LocalDateTime.now))
        .map(meas => meas.toJson)

    val sink = Sink.ignore

    val tabIn = Flow[String].wireTap(req => tracef(req, "--> %s", tracingEnabled))
    val tabOut = Flow[String].wireTap(res => tracef(res, "<-- %s", tracingEnabled))

    source.via(tabIn).via(service).via(tabOut).runWith(sink)
  end handleMessages

  
  def withTracing(enabled: Boolean = true): ServerSimulator =
    tracingEnabled = enabled
    this
  end withTracing

end ServerSimulator
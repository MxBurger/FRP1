package frp.basics.actors.task3

import akka.actor.typed.{ActorRef, ActorSystem, Behavior, SupervisorStrategy}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors, Routers}
import akka.util.Timeout
import java.time.Instant
import java.nio.file.{Files, Paths, StandardOpenOption}
import scala.concurrent.duration._
import scala.util.{Random, Success, Failure}

object WeatherStation {
  sealed trait Command
  case object Measure extends Command

  final case class WeatherStationConfig(
                                         measurementInterval: FiniteDuration,
                                         minTemp: Double,
                                         maxTemp: Double,
                                         storageActor: ActorRef[DataStorage.Command]
                                       )

  final case class Measurement(
                                id: String,
                                timestamp: Instant,
                                temperature: Double
                              )

  def apply(config: WeatherStationConfig): Behavior[Command] = {
    Behaviors.withTimers { timers =>
      timers.startTimerWithFixedDelay(
        "measurement",
        Measure,
        config.measurementInterval
      )

      Behaviors.receiveMessage {
        case Measure =>
          val measurement = Measurement(
            id = java.util.UUID.randomUUID().toString,
            timestamp = Instant.now(),
            temperature = config.minTemp + Random.nextDouble() * (config.maxTemp - config.minTemp)
          )

          config.storageActor ! DataStorage.StoreMeasurement(measurement)

          Behaviors.same
      }
    }
  }
}
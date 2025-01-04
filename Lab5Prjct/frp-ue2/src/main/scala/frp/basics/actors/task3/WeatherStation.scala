package frp.basics.actors.task3

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

import java.time.Instant
import scala.concurrent.duration.FiniteDuration
import scala.util.Random

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
    Behaviors.setup { context =>
      println(s"Starting WeatherStation with config: interval=${config.measurementInterval}, tempRange=[${config.minTemp}, ${config.maxTemp}]")

      Behaviors.withTimers { timers =>
        timers.startTimerWithFixedDelay(
          "measurement",
          Measure,
          config.measurementInterval
        )
        println(s"Measurement timer started with interval ${config.measurementInterval}")

        Behaviors.receiveMessage {
          case Measure =>
            val measurement = Measurement(
              id = java.util.UUID.randomUUID().toString,
              timestamp = Instant.now(),
              temperature = config.minTemp + Random.nextDouble() * (config.maxTemp - config.minTemp)
            )

            println(s"Generated measurement: id=${measurement.id}, temp=${measurement.temperature}")
            config.storageActor ! DataStorage.StoreMeasurement(measurement)

            Behaviors.same
        }
      }
    }
  }
}
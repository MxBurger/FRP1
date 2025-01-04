package frp.basics.actors.task3

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import scala.concurrent.duration._

@main
def main(): Unit = {
  val system = ActorSystem(
    Behaviors.setup[Nothing] { context =>
      // Create storage with worker pool
      val storageActor = context.spawn(
        DataStorage(DataStorage.StorageConfig(
          bufferSize = 100,
          bufferTimeout = 5.seconds,
          numberOfWorkers = 4
        )),
        "data-storage"
      )

      // Create weather station
      val weatherStation = context.spawn(
        WeatherStation(
          WeatherStation.WeatherStationConfig(
            measurementInterval = 100.millis, // High frequency for testing
            minTemp = -10.0,
            maxTemp = 40.0,
            storageActor = storageActor
          )
        ),
        "weather-station"
      )

      Behaviors.empty
    },
    "weather-station-system"
  )

  // System will run until terminated
}
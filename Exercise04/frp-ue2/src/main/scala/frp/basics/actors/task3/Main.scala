package frp.basics.actors.task3

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import scala.concurrent.duration._

@main
def main(): Unit = {
  val system = ActorSystem(
    Behaviors.setup[Nothing] { context =>
      println("Initializing WeatherStationSystem")

      val storageConfig = DataStorage.StorageConfig(
        bufferSize = 100,
        bufferTimeout = 5.seconds,
        numberOfWorkers = 4
      )

      // Create storage with worker pool
      val storageActor = context.spawn(
        DataStorage(storageConfig),
        "data-storage"
      )
      println("DataStorage actor created")

      val stationConfig = WeatherStation.WeatherStationConfig(
        measurementInterval = 100.millis,
        minTemp = -10.0,
        maxTemp = 40.0,
        storageActor = storageActor
      )

      // Create weather station
      val weatherStation = context.spawn(
        WeatherStation(stationConfig),
        "weather-station"
      )
      println("WeatherStation actor created")

      println("WeatherStationSystem fully initialized")

      Behaviors.empty
    },
    "weather-station-system"
  )
}
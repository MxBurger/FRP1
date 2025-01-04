package frp.basics.actors.task4

import akka.NotUsed
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.stream.scaladsl.*
import akka.util.ByteString

import java.nio.file.Paths
import java.time.Instant
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.*
import scala.util.Random


object WeatherStationStream {
  case class Measurement(
                          id: String,
                          timestamp: Instant,
                          temperature: Double
                        )

  case class WeatherStationConfig(
                                   measurementInterval: FiniteDuration = 100.milliseconds,
                                   minTemp: Double = 0.0,
                                   maxTemp: Double = 35.0,
                                   filePath: String = "measurements_stream.csv",
                                   parallelism: Int = 4,
                                   bufferSize: Int = 10
                                 )

  def createWeatherStationSource(config: WeatherStationConfig): Source[Measurement, NotUsed] = {
    val measurementGenerator = () => {
      val measurement = Measurement(
        id = java.util.UUID.randomUUID().toString,
        timestamp = Instant.now(),
        temperature = config.minTemp + Random.nextDouble() * (config.maxTemp - config.minTemp)
      )
      println(s"Generated measurement: id=${measurement.id}, temp=${measurement.temperature}")
      measurement
    }

    Source.repeat(NotUsed)
      .throttle(1, config.measurementInterval)
      .map(_ => measurementGenerator())
  }

  def createStorageFlow(config: WeatherStationConfig)(implicit ec: ExecutionContext): Flow[Measurement, ByteString, NotUsed] = {
    Flow[Measurement]
      .grouped(config.bufferSize)
      .mapAsync(config.parallelism) { measurements =>
        Future {
          println(s"Processing batch of ${measurements.size} measurements in thread ${Thread.currentThread().getName}")
          Thread.sleep(2000) // Simulate slow processing
          val data = measurements.map(m =>
            s"${m.id},${m.timestamp},${m.temperature}"
          ).mkString("\n") + "\n"
          ByteString(data)
        }
      }
  }

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "WeatherStationSystem")
    implicit val ec: ExecutionContext = system.executionContext
    val config = WeatherStationConfig()
    println(s"Starting stream processing with config: $config")

    val weatherSource = createWeatherStationSource(config)
    val storageFlow = createStorageFlow(config)
    val fileSink = FileIO.toPath(Paths.get(config.filePath))

    val graph = weatherSource
      .buffer(config.bufferSize, akka.stream.OverflowStrategy.backpressure)
      .via(storageFlow)
      .to(fileSink)

    val future = graph.run()
  }
}
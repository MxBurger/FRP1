package frp.basics.actors.task3

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

import java.nio.file.{Files, Paths, StandardOpenOption}
import scala.util.{Failure, Random, Success, Try}

object StorageWorker {
  sealed trait Command
  final case class StoreMeasurements(measurements: Vector[WeatherStation.Measurement]) extends Command

  def apply(workerId: Int, filePath: String): Behavior[Command] = {
    Behaviors.setup { context =>
      println(s"Initializing StorageWorker-${workerId} with filePath: ${filePath}")

      Behaviors.receiveMessage {
        case StoreMeasurements(measurements) =>
          println(s"Worker-${workerId} received ${measurements.size} measurements to persist")

          Try {
            // Simulate some processing time
            val processingTime = Random.nextInt(100)
            println(s"Worker-${workerId} processing for ${processingTime}ms")
            Thread.sleep(processingTime)

            // Persist to file
            val data = measurements.map(m =>
              s"${m.id},${m.timestamp},${m.temperature}"
            ).mkString("\n") + "\n"

            Files.write(
              Paths.get(filePath),
              data.getBytes,
              StandardOpenOption.CREATE,
              StandardOpenOption.APPEND
            )

            println(s"Worker-${workerId} successfully persisted ${measurements.size} measurements")
          } match {
            case Success(_) =>
              println(s"Worker-${workerId} completed successfully")
            case Failure(ex) =>
              println(s"Worker-${workerId} failed to persist measurements: ${ex.getMessage}")
          }

          Behaviors.same
      }
    }
  }
}

package frp.basics.actors.task3

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

import java.nio.file.{Files, Paths, StandardOpenOption}
import scala.util.Random

object StorageWorker {
  sealed trait Command
  final case class StoreMeasurements(measurements: Vector[WeatherStation.Measurement]) extends Command

  def apply(workerId: Int, filePath: String): Behavior[Command] = {
    Behaviors.setup { context =>
      Behaviors.receiveMessage {
        case StoreMeasurements(measurements) =>
          context.log.info(s"Worker $workerId persisting ${measurements.size} measurements")

          // Simulate some processing time
          Thread.sleep(Random.nextInt(100))

          // Actually persist to file
          val data = measurements.map(m =>
            s"${m.id},${m.timestamp},${m.temperature}"
          ).mkString("\n") + "\n"

          Files.write(
            Paths.get(filePath),
            data.getBytes,
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND
          )

          context.log.info(s"Worker $workerId finished persisting ${measurements.size} measurements")
          Behaviors.same
      }
    }
  }
}

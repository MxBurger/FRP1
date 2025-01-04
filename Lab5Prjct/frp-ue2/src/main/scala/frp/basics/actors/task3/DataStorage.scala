package frp.basics.actors.task3

import akka.actor.typed.{ActorRef, Behavior, SupervisorStrategy}
import akka.actor.typed.scaladsl.{Behaviors, Routers}

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.duration.FiniteDuration

object DataStorage {
  sealed trait Command
  final case class StoreMeasurement(measurement: WeatherStation.Measurement) extends Command
  private case object BufferTimeout extends Command
  private case class WorkerCompleted(success: Boolean) extends Command

  final case class StorageConfig(
                                  bufferSize: Int = 100,
                                  bufferTimeout: FiniteDuration = FiniteDuration(5, "seconds"),
                                  filePath: String = "measurements.csv",
                                  numberOfWorkers: Int = 4
                                )

  def apply(config: StorageConfig): Behavior[Command] = {
    Behaviors.setup { context =>
      // Create worker pool
      val workerPool = Routers.pool(config.numberOfWorkers) {
        val workerIdCounter = new AtomicInteger(0)
        Behaviors.supervise(
          StorageWorker(workerIdCounter.getAndIncrement(), config.filePath)
        ).onFailure[Exception](SupervisorStrategy.restart)
      }
      val router = context.spawn(workerPool, "storage-worker-pool")

      // Start periodic buffer flush
      Behaviors.withTimers { timers =>
        timers.startTimerWithFixedDelay(
          "bufferTimeout",
          BufferTimeout,
          config.bufferTimeout
        )

        active(Vector.empty, router, config)
      }
    }
  }

  private def active(
                      buffer: Vector[WeatherStation.Measurement],
                      router: ActorRef[StorageWorker.Command],
                      config: StorageConfig
                    ): Behavior[Command] = {
    Behaviors.receive { (context, message) =>
      message match {
        case StoreMeasurement(measurement) =>
          val newBuffer = buffer :+ measurement
          context.log.debug(s"Buffer size: ${newBuffer.size}")

          if (newBuffer.size >= config.bufferSize) {
            router ! StorageWorker.StoreMeasurements(newBuffer)
            active(Vector.empty, router, config)
          } else {
            active(newBuffer, router, config)
          }

        case BufferTimeout =>
          if (buffer.nonEmpty) {
            router ! StorageWorker.StoreMeasurements(buffer)
            active(Vector.empty, router, config)
          } else {
            Behaviors.same
          }

        case WorkerCompleted(success) =>
          if (!success) {
            context.log.error("Worker failed to persist measurements")
          }
          Behaviors.same
      }
    }
  }
}
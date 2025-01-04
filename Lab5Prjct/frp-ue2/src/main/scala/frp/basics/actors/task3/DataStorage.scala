package frp.basics.actors.task3

import akka.actor.typed.{ActorRef, Behavior, SupervisorStrategy}
import akka.actor.typed.scaladsl.{Behaviors, Routers}

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration.DurationInt

object DataStorage {
  sealed trait Command
  final case class StoreMeasurement(measurement: WeatherStation.Measurement) extends Command
  private case object BufferTimeout extends Command
  private case class WorkerCompleted(success: Boolean) extends Command

  final case class StorageConfig(
                                  bufferSize: Int = 100,
                                  bufferTimeout: FiniteDuration = 5.seconds,
                                  filePath: String = "measurements.csv",
                                  numberOfWorkers: Int = 4
                                )

  def apply(config: StorageConfig): Behavior[Command] = {
    Behaviors.setup { context =>
      println(s"Starting DataStorage with config: bufferSize=${config.bufferSize}, timeout=${config.bufferTimeout}, workers=${config.numberOfWorkers}")

      val workerIdCounter = new AtomicInteger(0)

      // Create worker pool
      val workerPool = Routers.pool(config.numberOfWorkers) {
        Behaviors.setup[StorageWorker.Command] { context =>
          val workerId = workerIdCounter.getAndIncrement()
          StorageWorker(workerId, config.filePath)
        }
      }
      val router = context.spawn(workerPool, "storage-worker-pool")
      println(s"Created worker pool with ${config.numberOfWorkers} workers")

      // Start periodic buffer flush
      Behaviors.withTimers { timers =>
        timers.startTimerWithFixedDelay(
          "bufferTimeout",
          BufferTimeout,
          config.bufferTimeout
        )
        println(s"Buffer timeout timer started with interval ${config.bufferTimeout}")

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
          println(s"Added measurement to buffer. Current size: ${newBuffer.size}/${config.bufferSize}")

          if (newBuffer.size >= config.bufferSize) {
            println(s"Buffer size threshold reached (${config.bufferSize}). Triggering persistence.")
            router ! StorageWorker.StoreMeasurements(newBuffer)
            active(Vector.empty, router, config)
          } else {
            active(newBuffer, router, config)
          }

        case BufferTimeout =>
          if (buffer.nonEmpty) {
            println(s"Buffer timeout triggered. Persisting ${buffer.size} measurements")
            router ! StorageWorker.StoreMeasurements(buffer)
            active(Vector.empty, router, config)
          } else {
            println("Buffer timeout triggered but buffer is empty")
            Behaviors.same
          }

        case WorkerCompleted(success) =>
          if (!success) {
            println("Worker reported failed persistence")
          } else {
            println("Worker reported successful persistence")
          }
          Behaviors.same
      }
    }
  }
}
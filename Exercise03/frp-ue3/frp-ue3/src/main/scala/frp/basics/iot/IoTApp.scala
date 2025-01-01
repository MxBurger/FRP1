package frp.basics.iot

import akka.NotUsed
import akka.actor.typed.scaladsl.AskPattern.*
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.stream.scaladsl.Flow
import akka.util.Timeout
import frp.basics.DefaultActorSystem
import frp.basics.MeasureUtil.measure

import scala.annotation.unused
import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, ExecutionContext, Future}


@main
def ioTApp(): Unit =
  val NR_MESSAGES = 200
  val MESSAGES_PER_SECOND = 100
  val PARALLELISM = 4 // Runtime.getRuntime.availableProcessors
  val BULK_SIZE = 10
  val TIME_WINDOW = 200.millis

  val actorSystem: ActorSystem[DatabaseActor.Command] =
    DefaultActorSystem(DatabaseActor(BULK_SIZE, TIME_WINDOW, PARALLELISM))
  given ExecutionContext = actorSystem.executionContext

  def identityService(): Flow[String, String, NotUsed] = Flow[String].map(identity)

  def simpleMeasurementsService(parallelism: Int = PARALLELISM): Flow[String, String, NotUsed] =
    val repository= Repository().withTracing()

    Flow[String]
      .mapAsync(parallelism)(json => Future {
        Measurement.fromJson(json)
      })
      .collect{case Right(meas) => meas}
      .mapAsync(parallelism)(repository.insertAsync)
      .map(meas => meas.ack().toJson)


  end simpleMeasurementsService


  def measurementsServiceWithActor(dbActor: ActorRef[DatabaseActor.Command], parallelism: Int = PARALLELISM): Flow[String, String, NotUsed] =
    given ActorSystem[DatabaseActor.Command] = actorSystem
    given Timeout = 500.millis

    Flow[String]
      .mapAsync(parallelism)(json => Future {
        Measurement.fromJson(json)
      })
      .collect{case Right(meas) => meas}
      .mapAsync[Acknowledgement](BULK_SIZE * parallelism){
        meas =>
          dbActor ? (DatabaseActor.Insert(meas, _)) // ? operator is a shorthand for ask (returns a Future (successful or failed))
      }
      .map(ack => ack.toJson)

  end measurementsServiceWithActor

  def measurementsService(parallelism: Int = PARALLELISM): Flow[String, String, NotUsed] =
    val repository = Repository().withTracing()

    Flow[String]
      .mapAsync(parallelism)(json => Future {
        Measurement.fromJson(json)
      })
      .collect { case Right(meas) => meas }
      .groupedWithin(BULK_SIZE, TIME_WINDOW)
      .mapAsync(parallelism)(measurements => repository.bulkInsertAsync(measurements)) // could also written with method-reference
      .mapConcat(meas => meas.map(m => m.ack().toJson))
  end measurementsService



  val server = ServerSimulator(NR_MESSAGES, MESSAGES_PER_SECOND).withTracing(true)

//  println("============================== identityService ==============================");
//  val done1 = server.handleMessages(identityService())

//  println("============================== simpleMeasurementService ==============================");
//  val done2 = server.handleMessages(simpleMeasurementsService())

//  println("============================== measurementsServiceWithActor ==============================");
//  val done3 = server.handleMessages(measurementsServiceWithActor(dbActor = actorSystem))

  println("============================== measurementsService ==============================")


  val done4 =
    measure{
      server.handleMessages(measurementsService())
    } { duration =>
      println(s"Total time: $duration")
      println(s"throughput: ${NR_MESSAGES / duration} msg/s")
    }


  Await.result(done4, Duration.Inf)

  Await.result(DefaultActorSystem.terminate(), Duration.Inf)

end ioTApp
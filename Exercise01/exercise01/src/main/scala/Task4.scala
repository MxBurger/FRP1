package com.fhooe

import scala.concurrent.{Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

// a)
// Simulated data sources
def dataSourceA(): Future[String] = Future {
  Thread.sleep(500)
  "Result from DataSource A"
}

def dataSourceB(): Future[String] = Future {
  Thread.sleep(300)
  "Result from DataSource B"
}

def dataSourceC(): Future[String] = Future {
  Thread.sleep(700)
  "Result from DataSource C"
}

object FutureExtensions {
  implicit class CompetitiveFuture[T](val future: Future.type) {
    def doCompetitively(futures: List[Future[T]]): Future[T] = {
      // Create a promise to hold the first completed result
      val promise = Promise[T]()
      futures.foreach { future =>
        future.onComplete {
          // Try to complete the promise with the first result (either success or failure)
          promise.tryComplete(_: Try[T])
        }
      }
      promise.future
    }
  }
}

object Task4a extends App {

  // a)
  val whoeverIsFirst: Future[String] = Future.firstCompletedOf(
    List(dataSourceA(), dataSourceB(), dataSourceC())
  )

  whoeverIsFirst.onComplete {
    case Success(result) =>
      println(s"First completed result: $result")
    case Failure(ex) =>
      println(s"An error occurred: ${ex.getMessage}")
  }


  // b)

  import FutureExtensions.CompetitiveFuture

  val whoIsFirstAgain: Future[String] = Future.doCompetitively(
    List(dataSourceA(), dataSourceB(), dataSourceC())
  )

  whoIsFirstAgain.onComplete {
    case Success(result) =>
      println(s"First completed result from own implementation: $result")
    case Failure(ex) =>
      println(s"An error occurred: ${ex.getMessage}")
  }

  Thread.sleep(1000)
}

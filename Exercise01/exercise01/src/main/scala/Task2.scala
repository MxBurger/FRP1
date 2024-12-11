package com.fhooe

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Random, Success}

// a)
def doInParallel(a: => Unit, b: => Unit): Future[Unit] = {
  val f1 = Future(a)
  val f2 = Future(b)

  f1.flatMap(_ => f2).recoverWith {
    case e1 =>
      f2.recoverWith { case _ => Future.failed(e1) }
  }
}

// b) exception handling not specified

// b) with  for comprehension
def doInParallel[U,V](f1: Future[U], f2: Future[V]): Future[(U,V)] = {
  for {
    result1 <- f1
    result2 <- f2
  } yield (result1, result2)
}

// b) with flatMap
//def doInParallel[U,V](f1: Future[U], f2: Future[V]): Future[(U,V)] = {
//  f1.flatMap(result1 => f2.map(result2 => (result1, result2)))
//}

// c) with Future.zip
// def doInParallel[U,V](f1: Future[U], f2: Future[V]): Future[(U,V)] = {
//   f1.zip(f2)
// }

// d) i don't know, which "merge from a)" was meant, so I use this one
def merge(left: List[Int], right: List[Int]): List[Int] = {
  (left, right) match {
    case (Nil, _) => right
    case (_, Nil) => left
    case (x :: xs, y :: ys) =>
      if (x < y) x :: merge(xs, right)
      else y :: merge(left, ys)
  }
}

object Task2a extends App {
  // Tests for a)

  // both blocks complete successfully
  val f1 = doInParallel(
    {
      Thread.sleep(1000)
      println("block 1 done")
    },
    {
      Thread.sleep(1000)
      println("block 2 done")
    }
  )
  f1.onComplete(_ => println("f1 done"))

  // block 4 fails, but f2 still completes
  val f2 = doInParallel(
    {
      Thread.sleep(1000)
      println("block 3 done")
    },
    {
      Thread.sleep(1000)
      throw new Exception("block 4 failed")
      println("block 4 done")
    }
  )
  f2.onComplete(_ => println("f2 done"))

  // block 5 fails, but f3 still completes
  val f3 = doInParallel(
    {
      Thread.sleep(1000)
      throw new Exception("block 5 failed")
      println("block 5 done")
    },
    {
      Thread.sleep(1000)
      println("block 6 done")
    }
  )
  f3.onComplete(_ => println("f3 done"))

  // both blocks fail, but f4 still completes
  val f4 = doInParallel(
    {
      Thread.sleep(1000)
      throw new Exception("block 7 failed")
      println("block 7 done")
    },
    {
      Thread.sleep(1000)
      throw new Exception("block 8 failed")
      println("block 8 done")
    }
  )
  f4.onComplete(_ => println("f4 done"))

  Thread.sleep(3000)
}

object Task2bc extends App {
  // Tests for b)

  // Combining two successful futures
  val future1 = Future{ Thread.sleep(2000); 42 }
  val future2 = Future{ Thread.sleep(1000); "Hello" }

  val combinedFuture = doInParallel(future1, future2)
  combinedFuture.onComplete {
    case Success((result1, result2)) => println(s"Results: $result1, $result2")
    case Failure(exception) => println(s"Failed with exception: $exception")
  }

  // One future fails
  val future3 = Future{ Thread.sleep(1500); new Exception("Failure in future3"); }
  val future4 = Future("Hello")

  val combinedFuture2 = doInParallel(future3, future4)
  combinedFuture2.onComplete {
    case Success((result1, result2)) => println(s"Results: $result1, $result2")
    case Failure(exception) => println(s"Failed with exception: $exception")
  }

  // Both futures fail
  val future5 = Future{ Thread.sleep(1000); new Exception("Failure in future5") }
  val future6 = Future{ Thread.sleep(1000); new Exception("Failure in future6") }

  val combinedFuture3 = doInParallel(future5, future6)
  combinedFuture3.onComplete {
    case Success((result1, result2)) => println(s"Results: $result1, $result2")
    case Failure(exception) => println(s"Failed with exception: $exception")
  }
  Thread.sleep(3000);
}

object Task2d extends App {
  val randomNumbers = List.fill(20)(Random.nextInt(100))
  println(s"Original list: $randomNumbers")

  val (left, right) = randomNumbers.splitAt(randomNumbers.length / 2)

  val sortedFutures = doInParallel(Future(left.sorted), Future(right.sorted))

  sortedFutures.onComplete {
    case Success((sortedLeft, sortedRight)) =>
      val merged = merge(sortedLeft, sortedRight)
      println(s"Merged list: $merged")
    case Failure(exception) => println(s"Failed with exception: $exception")
  }


}
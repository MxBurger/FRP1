package com.fhooe

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Random, Success}

// a)
def parallelMax1(list: List[Int], nParts: Int): Future[List[Int]] = {
  if(list.isEmpty){ return Future.failed(new IllegalArgumentException("List is empty")) }
  val partSize = list.size / nParts
  val parts: List[List[Int]] = list.grouped(partSize).toList
  val futures: List[Future[Int]] = parts.map(part => Future(part.max))
  val future: Future[List[Int]] = Future.sequence(futures)
  future
}

// b)
def parallelMax2(list: List[Int], nParts: Int): Future[List[Int]] = {
  if(list.isEmpty){ return Future.failed(new IllegalArgumentException("List is empty")) }
  val partSize = list.size / nParts
  val parts: List[List[Int]] = list.grouped(partSize).toList
  val futures: List[Future[Int]] = parts.map(part => Future(part.max))
  // i want to use foldLeft to get the max from every List in parts
  val future: Future[List[Int]] = Future.foldLeft(futures)(List.empty[Int])((acc, elem) => acc ++ List(elem))
  future
}

// c)
def awesomeSequence[T](futures: List[Future[T]]): Future[List[T]] = {
  futures match {
    case Nil => Future.successful(Nil)
    case head :: tail =>
      val tailResult = awesomeSequence(tail)
      for {
        h <- head // wait for the completion of 'head'
        t <- tailResult // wait for the completion of 'tail'
      } yield h :: t
  }
}

// usage of c)
def parallelMax1_3(list: List[Int], nParts: Int): Future[List[Int]] = {
  if (list.isEmpty) {
    return Future.failed(new IllegalArgumentException("List is empty"))
  }
  val partSize = list.size / nParts
  val parts: List[List[Int]] = list.grouped(partSize).toList
  val futures: List[Future[Int]] = parts.map(part => Future(part.max))
  val future: Future[List[Int]] = awesomeSequence(futures)
  future
}


object Task3a extends App {
  // Tests for a)
  val list = List.fill(10)(Random.nextInt(100))
  println(s"List: $list")
  val result = parallelMax1(list, 3)
  result.onComplete {
    case Success(value) => println(s"Max: $value")
    case Failure(exception) => println(s"Error: ${exception.getMessage}")
  }

  val list2 = List.empty
  println(s"List: $list2")
  val result2 = parallelMax1(list2, 3)
  result2.onComplete {
    case Success(value) => println(s"Max: $value")
    case Failure(exception) => println(s"Error: ${exception.getMessage}")
  }

  Thread.sleep(5000)
}

object Task3b extends App {
  // Tests for b)
  val list = List.fill(10)(Random.nextInt(100))
  println(s"List: $list")
  val result = parallelMax2(list, 3)
  result.onComplete {
    case Success(value) => println(s"Max: $value")
    case Failure(exception) => println(s"Error: ${exception.getMessage}")
  }

  val list2 = List.empty
  println(s"List: $list2")
  val result2 = parallelMax2(list2, 3)
  result2.onComplete {
    case Success(value) => println(s"Max: $value")
    case Failure(exception) => println(s"Error: ${exception.getMessage}")
  }

  Thread.sleep(5000)
}

object Task3c extends App {
  // Tests for b)
  val list = List.fill(10)(Random.nextInt(100))
  println(s"List: $list")
  val result = parallelMax1_3(list, 3)
  result.onComplete {
    case Success(value) => println(s"Max: $value")
    case Failure(exception) => println(s"Error: ${exception.getMessage}")
  }

  val list2 = List.empty
  println(s"List: $list2")
  val result2 = parallelMax1_3(list2, 3)
  result2.onComplete {
    case Success(value) => println(s"Max: $value")
    case Failure(exception) => println(s"Error: ${exception.getMessage}")
  }

  Thread.sleep(5000)
}
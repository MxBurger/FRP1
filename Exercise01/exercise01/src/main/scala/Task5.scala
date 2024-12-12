package com.fhooe

import java.util.concurrent.{Executors, ForkJoinPool}
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits.global

// a)

def quickSort[T](seq: Seq[T])(using ord: Ordering[T]): Seq[T] = {
  if seq.length <= 1 then seq
  else {
    val pivot = seq(seq.length / 2)
    Seq.concat(quickSort(seq filter (ord.lt(_, pivot))),
      seq filter (ord.equiv(_, pivot)),
      quickSort(seq filter (ord.gt(_, pivot))))
  }
}

// with implicit Orderings (explicit are used in b) )

def quickSortDescending(seq: Seq[Int]): Seq[Int] = {
  given Ordering[Int] with {
    def compare(x: Int, y: Int): Int = x.compareTo(y) * -1 // reverse order
  }
  quickSort(seq)
}

def quickSortAscending(seq: Seq[Int]): Seq[Int] = {
  given Ordering[Int] with {
    def compare(x: Int, y: Int): Int = x.compareTo(y)
  }
  quickSort(seq)
}

def quickSortEven(seq: Seq[Int]): Seq[Int] = {
  given Ordering[Int] with {
    def compare(x: Int, y: Int): Int = {
      if x % 2 == 0 && y % 2 == 0 then x.compareTo(y)
      else if x % 2 == 0 then -1
      else if y % 2 == 0 then 1
      else x.compareTo(y)
    }
  }
  quickSort(seq)
}

// b)
// Parallel quicksort with configurable threshold and ExecutionContext
// This implementation is not correct, it freezes when too many Futures are created.
def quickSortPar[T](seq: Seq[T], threshold: Int)
                   (using ord: Ordering[T], ec: ExecutionContext): Seq[T] = {
  if (seq.length <= 1) seq
  else if (seq.length <= threshold) quickSort(seq) // Use sequential for small lists
  else {
    val pivot = seq(seq.length / 2)
    val ll = seq.filter(ord.lt(_, pivot))
    val rl = seq.filter(ord.gt(_, pivot))
    val eq = seq.filter(ord.equiv(_, pivot))
    val lF = Future(quickSortPar(ll, threshold))
    val rF = Future(quickSortPar(rl, threshold))

    // Wait for both futures and combine results
    val result = Await.result(
      for {
        l <- lF
        r <- rF
      } yield l ++ eq ++ r,
      Duration.Inf
    )
    result
  }
}

// Timing helper function
def time[T](f: => T, iterations: Int): Double = {
  val times = (1 to iterations).map { _ =>
    val start = System.nanoTime()
    f
    val end = System.nanoTime()
    (end - start) / 1_000_000 // Convert to milliseconds
  }
  times.sum / iterations
}

// Test function with different ExecutionContexts
def runPerformanceTests(data: Seq[Int], thresholds: Seq[Int],
                        sizes: Seq[Int], iterations: Int = 5): Unit = {
  println("Performance Testing Results:")
  println("===========================")

  given globalEC: ExecutionContext = global
  given fixedEC: ExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(8))
  given forkJoinEC: ExecutionContext = ExecutionContext.fromExecutor(new ForkJoinPool())

  def testWithEC(name: String)(using ec: ExecutionContext): Unit = {
    for (threshold <- thresholds) {
      println(s"\nThread Pool: $name, Threshold: $threshold")
      println("Size\tTime (ms)")
      println("--------------------")

      for (size <- sizes) {
        val testData = Random.shuffle((1 to size).toList)
        val avgTime = time({
          quickSortPar(testData, threshold)
        }, iterations)
        println(f"$size%d\t$avgTime%.2f")
      }
    }
  }

  // Run tests with different ExecutionContexts
  testWithEC("Global EC")(using globalEC)
  //testWithEC("Fixed(4)")(using fixedEC)
  //testWithEC("ForkJoin")(using forkJoinEC)
}


object Task5a extends App {
  val numbers = List(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5)
  println(s"numbers: $numbers")
  // ascending order
  val sortedNumbers = quickSortAscending(numbers)
  println(s"sortedNumbers: $sortedNumbers")
  // descending order
  val sortedNumbers2 = quickSortDescending(numbers)
  println(s"sortedNumbers2: $sortedNumbers2")
  // even numbers first
  val sortedNumbers3 = quickSortEven(numbers)
  println(s"sortedNumbers3: $sortedNumbers3")

  val list = Random.shuffle((1 to 1000_000).toList)
  quickSort(list)
  println("Sequential quicksort completed.")

}

// This implementation is not correct, it freezes when too many Futures are created.
object Task5b extends App {
  val thresholds = Seq(100, 500, 1_000)
  val sizes = Seq(1_000, 10_000, 100_000, 1_000_000)

  runPerformanceTests(
    Random.shuffle((1 to 1000).toList),
    thresholds,
    sizes
  )
  println("\nAll tests completed.")
}
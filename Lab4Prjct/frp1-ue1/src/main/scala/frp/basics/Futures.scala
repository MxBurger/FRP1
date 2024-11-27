package frp.basics

import scala.util.Random

// noinspection Duplicates
object Futures:

  private def println(x: Any): Unit = Console.println(s"$x (thread id=${Thread.currentThread.threadId})")

  private def doWork(task: String, steps: Int): Unit =
    for (i <- 1 to steps)
      println(s"$task: $i")
      if (i == 6) throw new IllegalArgumentException()
      Thread.sleep(200)
  end doWork

  private def compute(task: String, n: Int): Int =
    for (i <- 1 to n)
      println(s"$task: $i")
      Thread.sleep(200)
    Random.nextInt(1000)
  end compute

  private def combine(value1: Int, value2: Int): Int =
    for (i <- 1 to 5)
      println(s"combine: $i")
      Thread.sleep(200)
    value1 + value2
  end combine

  private def sequentialInvocation(): Unit = {}

  private def simpleFutures(): Unit = {}

  private def futuresWithCallback(): Unit = {}

  private def futureComposition1(): Unit = {}

  private def futureComposition2(): Unit = {}

    def main(args: Array[String]): Unit =
      println(s"availableProcessors=${Runtime.getRuntime.availableProcessors}")

      println("==== sequentialInvocation ====")
      sequentialInvocation()

      println("\n==== simpleFutures ====")
      simpleFutures()

      println("\n==== futuresWithCallback ====")
      futuresWithCallback()

      println("\n==== futureComposition1 ====")
      futureComposition1()

      println("\n==== futureComposition2 ====")
      futureComposition2()
    end main

end Futures


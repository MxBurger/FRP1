package frp.basics

import scala.util.Try
import scala.util.Success
import scala.util.Failure

object Monads:

  private def traditionalErrorHandling(): Unit = {
    for (s <- Seq("2", "5", "0", "x"))
      try
        val res = 10/s.toInt
        println(s"'$s' -> $res")
      catch
        case ex:Throwable  => println(s"'$s' -> $ex")

  }

  def toInt(s: String): Try[Int] = Try { s.toInt }
  def divide(a: Int, b: Int): Try[Int] = Try { a / b }

  private def monadCallbacks(): Unit = {
    println("---------------------- toInt(s) --------------------------")
    for (s <- Seq("2", "5", "0", "x"))
      val res = toInt(s)
      println(s"'$s' -> $res")

    println("---------------------- toInt(s).foreach --------------------------")
    for (s <- Seq("2", "5", "0", "x"))
      toInt(s).foreach(res => println(s"'$s' -> $res"))

    println("---------------------- toInt(s).failed.foreach --------------------------")
    for (s <- Seq("2", "5", "0", "x"))
      toInt(s).failed.foreach { res => println(s"'$s' -> $res") }

    println("---------------------- toInt(s) match --------------------------")
    for (s <- Seq("2", "5", "0", "x"))
      toInt(s) match
        case Success(value) => println(s"'$s' -> $value")
        case Failure(ex) => println(s"'$s' -> exception $ex")


  }

  private def monadCombinators(): Unit = {
    println("---------------------- toInt(s) map divide --------------------------")
    for (s <- Seq("2", "5", "0", "x"))
      val res = toInt(s) map (divide(10, _))
      println(s"'$s' -> $res")

    println("---------------------- toInt(s) flatMap divide --------------------------")
    for (s <- Seq("2", "5", "0", "x"))
      val res = toInt(s) flatMap  (divide(10, _))
      println(s"'$s' -> $res")

    println("---------------------- for --------------------------")
    for (s <- Seq("2", "5", "0", "x"))
      val res = for(
        n <- toInt(s);
        q <- divide(10, n)) yield q
      println(s"'$s' -> $res")

    println("---------------------- toInt/divide flatMap --------------------------")
    for ((s1, s2) <- Seq(("10","5"), ("10","0"), ("10","x"), ("x", "0")))
      val res = toInt(s1) flatMap {
        a => toInt(s2) flatMap {
          b => divide(a, b)
        }
      }
      println(s"'$s1'/'$s2' -> $res")

    println("---------------------- toInt/divide for --------------------------")
    for ((s1, s2) <- Seq(("10", "5"), ("10", "0"), ("10", "x"), ("x", "0")))
      val res =
        for(
          a <- toInt(s1);
          b <- toInt(s2);
          q <- divide(a, b)
        ) yield q
      println(s"'$s1'/'$s2' -> $res")
  }

  def main(args: Array[String]): Unit =
    println("======== traditionalErrorHandling ===========")
    traditionalErrorHandling()
    println()

    println("============= monadCallbacks ================")
    monadCallbacks()
    println()

    println("============ monadCombinators ===============")
    monadCombinators()
    println()
  end main

end Monads
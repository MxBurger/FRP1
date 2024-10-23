package opt

import java.util.Scanner

// Task 5.1: Alternative values for None

object Task5_1App extends App {

  val scn = new Scanner(System.in)
  val bds: Map[String, Int] = Map("x" -> 1, "y" -> 4, "z" -> 0)

  // a) Test the value and then access it with get.

  val optX = ???

  val optU = ???

  // b)	Use the method getOrElse to specify an alternative value in case of missing value

  val xOptOrElse : Option[Int] = ???
  println(xOptOrElse.get)

  // c)	Use the method elseGet to read in an alternative value from the user (with Scanner scn) if a value is missing

  val uOptOrElse : Option[Int] = ???
  println(uOptOrElse.get)

}


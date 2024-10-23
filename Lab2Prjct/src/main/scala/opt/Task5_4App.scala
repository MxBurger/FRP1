package opt

import java.util.Scanner

// Task 5.4: Chaining unsafe access operations

object Task5_4App extends App {

  val scn = new Scanner(System.in)
  val bds: Map[String, Int] = Map("x" -> 1, "y" -> 4, "z" -> 0)

  // a) x / y

  val optR1 : Option[Int] = ???

  optR1 match {
    case Some(r) => println(s"x / y = $r")
    case None => println("x / y failed")
  }

  // b) x / z

  val optR2 : Option[Int] = ???

  optR2 match {
    case Some(r) => println(s"x / z = $r")
    case None => println("x / z failed")
  }


  // c) x / u

  val optR3 : Option[Int] = ???

  optR3 match {
    case Some(r) => println(s"x / u = $r")
    case None => println("x / u failed")
  }

  // d) x / (y * z)

  val optR4 : Option[Int] = ???

  optR4 match {
    case Some(r) => println(s"x / (y * z) = $r")
    case None => println("x / (y * z) failed")
  }

}


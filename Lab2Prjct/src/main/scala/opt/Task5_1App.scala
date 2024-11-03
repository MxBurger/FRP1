package opt

import java.util.Scanner

// Task 5.1: Alternative values for None

object Task5_1App extends App {

  val scn = new Scanner(System.in)
  val bds: Map[String, Int] = Map("x" -> 1, "y" -> 4, "z" -> 0)

  // a) Test the value and then access it with get.

  val optX = bds.get("x")
  if(optX.isDefined) {
    println(optX.get)
  } else {
    println("No value for x")
  }

  // better alternative with pattern matching
  optX match {
    case Some(v) => println(v)
    case None => println("No value for x")
  }

  val optU = bds.get("u")
  if(optU.isDefined) {
    println(optU.get)
  } else {
    println("No value for u")
  }

  // better alternative with pattern matching
  optU match {
    case Some(v) => println(v)
    case None => println("No value for u")
  }

  // b)	Use the method getOrElse to specify an alternative value in case of missing value
  // this example also demonstrates the call-by-name parameter of getOrElse
  val xOptOrElse : Option[Int] =
    bds.get("x").orElse( {
      println("Accessing u")
      bds.get("u")
    })
  println(xOptOrElse.get)

  val uOptOrElse: Option[Int] =
    bds.get("u").orElse({
      println("Accessing x")
      bds.get("x")
    })
  println(uOptOrElse.get)


  // c)	Use the method elseGet to read in an alternative value from the user (with Scanner scn) if a value is missing

  val xOptElseGet = bds.get("x").getOrElse({
    println("Input value for x")
    scn.nextInt()
  })
  println(xOptElseGet)

  val uOptOrElseGet = bds.get("u").getOrElse({
    println("Input value for u")
    scn.nextInt()
  })
  println(uOptOrElseGet)

  val xOptElse =
    bds.get("x").orElse({
      println("Input value for x")
      option {
        scn.nextInt()
      }
    })
  println(xOptElse)

  val uOptElse =
    bds.get("u").orElse({
      println("Input value for u")
      option {
        scn.nextInt()
      }
    })
  println(uOptElse)

}


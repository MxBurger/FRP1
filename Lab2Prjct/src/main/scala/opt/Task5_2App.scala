package opt

// Task 5.2: Pattern matching on Option values

object Task5_2App extends App {

  val bds: Map[String, Int] = Map("x" -> 1, "y" -> 4, "z" -> 0)

  val optX = bds.get("x")

  optX match {
    case Some(0) => println("Zero")
    case Some(x) if x > 0 => println("Positive")
    case Some(x) => println("Negative")
    case None => println("No value")
  }

}


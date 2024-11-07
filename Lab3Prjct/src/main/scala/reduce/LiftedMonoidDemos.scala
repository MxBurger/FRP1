package reduce

enum Course :
  case Math1, Math2, SW1, SW2, IS2, BS

object LiftedMonoidDemos {

  def main(args: Array[String]): Unit = {
    val assnResults : List[Map[String, Int]] =
      List(
        Map("Hans" -> 6, "Fritz" -> 7, "Anna" -> 9),
        Map("Hans" -> 8, "Anna" -> 7),
        Map("Fritz" -> 6, "Anna" -> 8)
      )

    val totalPoints =
      Reducible(assnResults).reduce(using Monoid.mapMonoid(using Monoid.intPlusMonoid))

    println(totalPoints)

    import Course.*

    val coursesTaken: List[Map[String, Set[Course]]] =
      List(
        Map("Hans" -> Set(SW1, Math1, IS2), "Fritz" -> Set(SW1, BS, Math1)),
        Map("Hans" -> Set(SW2, Math2, BS), "Fritz" -> Set(SW2, IS2, Math2))
      )

    println(coursesTaken)
    val mapMonoid : Monoid[Map[String, Set[Course]]] = Monoid.mapMonoid(using Monoid.setMonoid)
    val reducible =  Reducible.apply(coursesTaken)
    val allCoursesTaken = reducible.reduce(using mapMonoid)
    println(allCoursesTaken)
  }

}

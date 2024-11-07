package reduce

import reduce.Monoid.{intPlusMonoid, setMonoid, stringMonoid}

object Task6_2_ListReducible {

  def main(args: Array[String]): Unit = {

    import Monoid.*

    val names = List("Susi", "Fritz", "Hans", "Alois", "Josef", "Gust", "Peter")
    val namesReducible: Reducible[String] = Reducible.apply(names)

    // === Task 6.2 ====================

    //a) count the elements
    val n = namesReducible.reduceMap(name => 1) //(using intPlusMonoid) // not necessary to specify the monoid here, cause "given"
    println(s"Number elements = $n")

    //b) concatenate the elements to a single string
    val one = namesReducible.reduceMap(name => name) //(using stringMonoid) // not necessary to specify the monoid here, cause "given"
    println(s"Concatenated = $one")

    //c) compute length of all strings
    val length = namesReducible.reduceMap(name => name.length) //(using intPlusMonoid) // not necessary to specify the monoid here, cause "given"
    println(s"Length of elements = $length")

    //d) create a set of the elements
    val setOfNames : Set[String] = namesReducible.reduceMap(name => Set(name)) (using setMonoid[String])
    println(s"Set of elements = $setOfNames")
  }

}

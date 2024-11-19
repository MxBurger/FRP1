package reduce

import reduce.Monoid.setMonoid

object Task6_3_ListReducible {

  def main(args: Array[String]): Unit = {

    import Monoid.*

    val names = List("Susi", "Fritz", "Hans", "Alois", "Josef", "Gust", "Peter")
    val namesReducible = Reducible(names)

    // === Task 6.3 ====================

    //a) count the elements
    val n = namesReducible.count
    println(s"Number elements = $n")

    //b) compute length of all strings
    val length = namesReducible.sum(name => name.length)
    println(s"Length of elements = $length")

    //c) create a list of the elements
    val list = namesReducible.asList
    println(s"List of elements = $list")

    //d) create a set of the elements
    val setOfNames = namesReducible.asSet
    println(s"Set of elements = $setOfNames")
  }

}

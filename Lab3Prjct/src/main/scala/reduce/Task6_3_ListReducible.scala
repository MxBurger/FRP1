package reduce

object Task6_3_ListReducible {

  def main(args: Array[String]): Unit = {

    import Monoid.*

    val names = List("Susi", "Fritz", "Hans", "Alois", "Josef", "Gust", "Peter")
    val namesReducible = Reducible(names)

    // === Task 6.3 ====================

    //a) count the elements
    val n = ???
    println(s"Number elements = $n")

    //b) concatenate the elements to a single string
    val one = ???
    println(s"Concatenated = $one")

    //c) compute length of all strings
    val length = ???
    println(s"Length of elements = $length")

    //d) create a set of the elements
    val setOfNames = ???
    println(s"Set of elements = $setOfNames")
  }

}

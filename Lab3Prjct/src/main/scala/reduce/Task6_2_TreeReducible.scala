package reduce

import reduce.Monoid.setMonoid
import tree.*

object Task6_2_TreeReducible {

  def main(args: Array[String]): Unit = {

    import Monoid.*
    import tree.BinTree.*

    val nameTree : BinTree[String] =
      node("Susi",
        node(
          "Fritz",
          node(
            "Alois",
            node(
              "Gust", empty, empty
            ),
            node("Peter", empty, empty)
          ),
          node("Josef", empty, empty)
        ),
        node("Hans", empty, empty)
      )

    val namesReducible = Reducible(nameTree)

    val list = namesReducible.reduceMap(name => List(name)) // (using listMonoid[String])
    println(s"List of elements = $list")

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

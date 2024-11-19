package reduce

import reduce.Monoid.setMonoid
import tree.*

object Task6_3_TreeReducible {

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

    //a) count the elements
    val n = namesReducible.reduceMap(name => 1)
    println(s"Number elements = $n")

    //b) concatenate the elements to a single string
    val one = namesReducible.reduceMap(name => name)
    println(s"Concatenated = $one")

    //c) compute length of all strings
    val length = namesReducible.reduceMap(name => name.length)
    println(s"Length of elements = $length")

    //d) create a set of the elements
    val setOfNames = namesReducible.reduceMap(name => Set(name)) (using setMonoid[String])
    println(s"Set of elements = $setOfNames")
  }

}

package reduce

import reduce.Monoid.{setMonoid, stringMonoid}
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

package reduce

import tree._

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

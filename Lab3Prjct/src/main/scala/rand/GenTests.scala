package rand

import Gen.*

object GenTests {

  def main(args: Array[String]): Unit = {

    // TODO: Task 8.4.a) Generator of list of 10 Int values from 0 to 100
    val b10: Gen[List[Boolean]] = ???
    val (bs, _) = b10(3293)
    for (b <- bs) println(b)
    println

    // TODO: Task 8.4.b) Generator of list of 10 Boolean values with probability 0.5
    val i10: Gen[List[Boolean]] = ???
    val (is, _) = i10(40591)
    for (i <- is) println(i)
    println

    // TODO: Task 8.4.c) Generator for list with 10 lists with lenght between 2 and 10 of integers from 0 to 100
    val nIntListLists:  Gen[List[List[Int]]] = ???
    val (r1, _) = nIntListLists(34243)
    for (l <- r1)
      println(l)
    println

    // TODO: Task 8.4.d) Generator for list with 10 random words up to 10 characters
    val nWordsLists: Gen[List[String]] = ???
    val (r3, _) = nWordsLists(23987)
    for (l <- r3) println(l)
    println

    // TODO: Task 8.4.e) Generator for list with 10 randam values from Strings “A”, “B”, “C”
    val nElemsLists: Gen[List[String]] = ???
    val (r4, _) = nElemsLists(87236481)
    for (l <- r4) println(l)

  }
}

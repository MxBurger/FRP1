package rand

import Gen.*

object GenTests {

  def main(args: Array[String]): Unit = {

    val pairGen =
    for {
      i1 <- ints
      i2 <- ints
    } yield (i1, i2)

    val (p, n) = pairGen.apply(29437)
    println(p)

    // Task 8.4.a) Generator of list of 10 Int values from 0 to 100
    val i10: Gen[List[Int]] = intsTo(100).list(10)
    val (is, _) = i10(40591)
    for (i <- is) println(i)
    println

    // Task 8.4.b) Generator of list of 10 Boolen values with probability 0.5
    val b10: Gen[List[Boolean]] = Gen.booleans(0.5).list(10)
    val (bs, _) = b10(3293)
    for (b <- bs) println(b)
    println

    // Task 8.4.c) Generator for list with 10 lists with length between 2 and 10 of integers from 0 to 100
    val nIntListLists:  Gen[List[List[Int]]] = intsTo(100).listsOfLengths(2, 10)list(10)
    val (r1, _) = nIntListLists(34243)
    for (l <- r1)
      println(l)
    println

    // Task 8.4.d) Generator for list with 10 random words up to 10 characters
    val nWordsLists: Gen[List[String]] = words(10).list(10)
    val (r3, _) = nWordsLists(23987)
    for (l <- r3) println(l)
    println

    // Task 8.4.e) Generator for list with 10 random values from Strings “A”, “B”, “C”
    val nElemsLists: Gen[List[String]] = valuesOf("A", "B", "C").list(10)
    val (r4, _) = nElemsLists(87236481)
    for (l <- r4) println(l)

    // further tests

    // Generator for list of 5 pairs of random integers
    val intIntPairs: Gen[List[(Int, Int)]] = ints.flatMap(i1 => ints.map(i2 => (i1, i2))).list(5)
    val (r5, _) = intIntPairs(12345)
    for (pair <- r5) println(pair)
    println

    // Generator for nested lists of random strings
    val nestedStringLists: Gen[List[List[String]]] = words(15).listsOfLengths(3, 3).list(5)
    val (r6, _) = nestedStringLists(67890)
    for (list <- r6) println(list)
    println

    // Generator for lists of floating - point numbers in the range [ 0.0, 1.0 ]
    val floatList: Gen[List[Double]] = doublesTo(1.0).list(10)
    val (r7, _) = floatList(54321)
    for (d <- r7) println(d)
    println



  }
}

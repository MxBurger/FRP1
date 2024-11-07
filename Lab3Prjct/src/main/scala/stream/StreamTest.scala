package stream

object StreamTest extends App {

  import Stream._

  // TODO:  Task 9.4.a) Use iterate for defining an infinite stream of positive integers,
  //    filter out odd values, map them to their square and print out the first 10.
  val intsFrom1: Stream[Int] = ???
  // TODO
  println

  // TODO:  Task 9.4.b)	Use iterate for defining an infinite stream of powers of 2 starting by 2.
  //    Take the first 10 and create a list.
  val powerOf2: List[Int] = ???
  println(powerOf2)
  println

  val words = List("Hannah", "Ann", "Sepp", "Peter", "", "Paul", "Flora")
  // TODO:  Task 9.4.c)	Use from for creating a Stream from a list of words, filter out those of length 0,
  //    map them to their length and print out the result
  println

  // TODO:  Task 9.4.d)	Use from for creating a Stream from a list of words and
  //    find a word which contain a "A" or "a"". Create a list.
  val wordsA: List[String] = ???
  println(wordsA)

  // TODO:  Task 9.4.e)	Use iterate for creating a Stream of all integers
  //  starting with 1000 and find the first prime number (Use filter and headOption)
  val firstPrime: Option[Int] = ??? 
  println(firstPrime)

}

def isPrime(x: Int) : Boolean = {
  for (i <- 2 to Math.sqrt(x).toInt) {
    if (x % i == 0) return false
  }
  true
}
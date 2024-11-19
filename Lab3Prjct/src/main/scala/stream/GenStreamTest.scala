package stream

import rand.Gen
import rand.Gen.{booleans, intsFromTo, intsTo, valuesOf, words}

object GenStreamTest {

  def main(args: Array[String]): Unit = {

    //  Task 9.5.a) Create a stream from generator for integers to 100 and take 10 values and print them out
    val ints: Stream[Int] =
      Gen.intsTo(100).stream.take(10)
    ints.forEach(println)
    
    println
    
    //  Task 9.5.b) Create a stream from generator for words of maximal length 20 and take 10 values and print them out
    val words: Stream[String] =
      Gen.words(20).stream.take(10)
    words.forEach(println)
    
    println

    //  Task 9.5.c) Create a stream from generator for integers from 2 to 100 and find one which is a prime
    //  (use filter and headOption)
    val prime: Option[Int] =
      Gen.intsFromTo(2, 100).stream
        .filter(isPrime)
        .headOption

    if prime.isEmpty then println("No prime number found")
    else println(prime.get)
    
    println

    //  Task 9.5.d) Create a stream from generator for words and find one which contains "x"
    //  (use filter and headOption)
    val wordWithX: Option[String] =
      Gen.words(20).stream
        .filter(w => w.contains("x"))
        .headOption

    if wordWithX.isEmpty then println("No word with 'x' found")
    else println(wordWithX.get)
    
  }
}

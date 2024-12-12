package com.fhooe

import scala.util.Random

def quickSort[T](seq: Seq[T])(using ord: Ordering[T]): Seq[T] = {
  if seq.length <= 1 then seq
  else {
    val pivot = seq(seq.length / 2)
    Seq.concat(quickSort(seq filter (ord.lt(_, pivot))),
      seq filter (ord.equiv(_, pivot)),
      quickSort(seq filter (ord.gt(_, pivot))))
  }
}

def quickSortDescending(seq: Seq[Int]): Seq[Int] = {
  given Ordering[Int] with {
    def compare(x: Int, y: Int): Int = x.compareTo(y) * -1 // reverse order
  }
  quickSort(seq)
}

def quickSortAscending(seq: Seq[Int]): Seq[Int] = {
  given Ordering[Int] with {
    def compare(x: Int, y: Int): Int = x.compareTo(y)
  }
  quickSort(seq)
}

def quickSortEven(seq: Seq[Int]): Seq[Int] = {
  given Ordering[Int] with {
    def compare(x: Int, y: Int): Int = {
      if x % 2 == 0 && y % 2 == 0 then x.compareTo(y)
      else if x % 2 == 0 then -1
      else if y % 2 == 0 then 1
      else x.compareTo(y)
    }
  }
  quickSort(seq)
}

object Task5 extends App {
  val numbers = List(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5)
  println(s"numbers: $numbers")
  // ascending order
  val sortedNumbers = quickSortAscending(numbers)
  println(s"sortedNumbers: $sortedNumbers")
  // descending order
  val sortedNumbers2 = quickSortDescending(numbers)
  println(s"sortedNumbers2: $sortedNumbers2")
  // even numbers first
  val sortedNumbers3 = quickSortEven(numbers)
  println(s"sortedNumbers3: $sortedNumbers3")

}
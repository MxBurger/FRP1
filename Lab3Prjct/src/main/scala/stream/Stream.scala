package stream

import reduce._
import reduce.Monoid

sealed trait Stream[+A] {
  val isEmpty: Boolean

  // TODO: Task 9.2: Lazy methods in trait Stream
  def map[B](mapper: A => B): Stream[B] = ???
  def take(n: Int): Stream[A] = ???
  def filter(pred: A => Boolean): Stream[A] = ???

  // TODO: Task 9.3: Methods in trait Stream returning results
  def head: A = ???
  def tail: Stream[A] = ???
  def headOption: Option[A] = ???
  def tailOption: Option[Stream[A]] = ???
  def forEach(action: A => Unit): Unit = ???
  def toList: List[A] = ???
  def reduceMap[R](mapper: A => R) (using monoid: Monoid[R]): R = ???
  def count: Long = ???

}

case object Empty extends Stream[Nothing] {
  override val isEmpty = true
}

case class Cons[+A](hdFn: () => A, tlFn: () => Stream[A]) extends Stream[A] {
  override val isEmpty = false
}


object Stream {
  // TODO: Task 9.1: Factory methods
  def apply[A](hd: => A, tl: => Stream[A]): Stream[A] = ???

  def from[A](lst: List[A]): Stream[A] = ???

  def iterate[A](initial: A, nextFn: A => A): Stream[A] = ???

}

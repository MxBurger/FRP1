package stream

import reduce._
import reduce.Monoid

sealed trait Stream[+A] {
  val isEmpty: Boolean

  // TODO: Task 9.2: Lazy methods in trait Stream
  def map[B](mapper: A => B): Stream[B] =
    this match
      case Empty => Empty
      case Cons(hdFn, tlFn) => Cons(() => mapper(hdFn()), () => tlFn().map(mapper))

  def take(n: Int): Stream[A] =
    this match
      case Empty => Empty
      case Cons(hdFn, tlFn) =>
        if n <= 0 then Empty
        else Cons(hdFn, () => tlFn().take(n - 1))
  def filter(pred: A => Boolean): Stream[A] = ???

  // TODO: Task 9.3: Methods in trait Stream returning results
  def head: A = ???
  def tail: Stream[A] = ???
  def headOption: Option[A] =
    this match
      case Empty => None
      case Cons(tlFn, _) => Some(tlFn()) // force (materialize) the tail
  def tailOption: Option[Stream[A]] = ???
  def forEach(action: A => Unit): Unit =
    this match
      case Empty => ()
      case Cons(hdFn, tlFn) => {
        action(hdFn())
        tlFn().forEach(action)
      }
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
  def apply[A](hd: => A, tl: => Stream[A]): Stream[A] =
    Cons(() => hd, () => tl)

  def from[A](lst: List[A]): Stream[A] = ??? // HW

  def iterate[A](first: A, nextFn: A => A): Stream[A] =
    Cons(() => first, () => iterate(nextFn(first), nextFn))

}

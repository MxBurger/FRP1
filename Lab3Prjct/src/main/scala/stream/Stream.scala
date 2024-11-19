package stream

import reduce._
import reduce.Monoid
import reduce.Monoid._

sealed trait Stream[+A] {
  val isEmpty: Boolean

  //  Task 9.2: Lazy methods in trait Stream
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

  def filter(pred: A => Boolean): Stream[A] =
    this match
      case Empty => Empty
      case Cons(hdFn, tlFn) =>
        if pred(hdFn()) then Cons(hdFn, () => tlFn().filter(pred))
        else tlFn().filter(pred)

  //  Task 9.3: Methods in trait Stream returning results
  def head: A =
    this match
      case Empty => throw new NoSuchElementException("head of empty stream")
      case Cons(hdFn, _) => hdFn()

  def tail: Stream[A] =
    this match
      case Empty => throw new UnsupportedOperationException("tail of empty stream")
      case Cons(_, tlFn) => tlFn()

  def headOption: Option[A] =
    this match
      case Empty => None
      case Cons(tlFn, _) => Some(tlFn()) // force (materialize) the tail

  def tailOption: Option[Stream[A]] =
    this match
      case Empty => None
      case Cons(_, tlFn) => Some(tlFn()) // force (materialize) the tail

  def forEach(action: A => Unit): Unit =
    this match
      case Empty => ()
      case Cons(hdFn, tlFn) => {
        action(hdFn())
        tlFn().forEach(action)
      }

  def toList: List[A] =
    this match
      case Empty => List()
      case Cons(hdFn, tlFn) => hdFn() :: tlFn().toList

  def reduceMap[R](mapper: A => R) (using monoid: Monoid[R]): R =
    this match
      case Empty => monoid.zero
      case Cons(hdFn, tlFn) => monoid.op(mapper(hdFn()), tlFn().reduceMap(mapper))

  def count: Int = reduceMap(_ => 1)

}

case object Empty extends Stream[Nothing] {
  override val isEmpty = true
}

case class Cons[+A](hdFn: () => A, tlFn: () => Stream[A]) extends Stream[A] {
  override val isEmpty = false
}


object Stream {
  //  Task 9.1: Factory methods
  def apply[A](hd: => A, tl: => Stream[A]): Stream[A] =
    Cons(() => hd, () => tl)

  def from[A](lst: List[A]): Stream[A] = lst match
    case hd :: tl => Cons(() => hd, () => from(tl))
    case Nil => Empty

  def iterate[A](first: A, nextFn: A => A): Stream[A] =
    Cons(() => first, () => iterate(nextFn(first), nextFn))

}

package reduce

import Monoid.{intPlusMonoid, *}
import tree.{BinNode, BinTree, EmptyTree}

trait Reducible[A] {
  def reduceMap[B](mapper: A => B)(using monoid: Monoid[B]): B // "using" uses the default monoid defined with "given" in Monoid.scala
  def reduce(using monoid: Monoid[A]): A = reduceMap(a => a)

  def asList: List[A] = reduceMap(a => List(a)) // (using listMonoid[A])
  def asSet: Set[A] = reduceMap(a => Set(a)) // (using setMonoid[A])
  def count: Int = reduceMap(a => 1) // (using intPlusMonoid)
  def sum(fn: A => Int): Int = reduceMap(fn)
}

object Reducible {

  def apply[A](as: Iterable[A]): Reducible[A] =
    new Reducible[A] {
      override def reduceMap[B](mapper: A => B)(using monoid: Monoid[B]): B = {
        var result = monoid.zero
        for (a <- as) {
          result = monoid.op(result, mapper(a))
        }
        result
      }

    }

  def apply[A](tree: BinTree[A]): Reducible[A] = // HOMEWORK
    new Reducible[A] {
      override def reduceMap[B](mapper: A => B)(using monoid: Monoid[B]): B = {
        tree match {
          case EmptyTree => monoid.zero
          case BinNode(elem, left, right) => {
            val lv = apply(left).reduceMap(mapper)
            val rv = apply(right).reduceMap(mapper)
            val elemV = mapper(elem)
            monoid.op(monoid.op(lv, elemV), rv)
          }
        }
      }
    }

}


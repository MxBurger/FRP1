package tree

sealed trait BinTree[+A] {
  val isEmpty: Boolean

  def elems: Int =
    this match {
      case EmptyTree => 0
      case BinNode(x, l, r) => 1 + l.elems + r.elems
    }

  def map[B](fn: A => B): BinTree[B] =
    this match {
      case EmptyTree => EmptyTree
      case BinNode(e, l, r) => BinNode(fn(e), l.map(fn), r.map(fn))
    }

  def find(pred: A => Boolean): Option[A] =
    this match {
      case EmptyTree => None
      case BinNode(e, l, r) =>
        if (pred(e)) then Some(e)
        else {
          val lResult = l.find(pred)
          if (lResult.isDefined) then lResult
          else r.find(pred)
        }
    }

  def any(pred: A => Boolean): Boolean =
    find(pred).isDefined

  def all(pred: A => Boolean): Boolean =
    find(e => !pred(e)).isEmpty

}

case object EmptyTree extends BinTree[Nothing] :
  val isEmpty : Boolean = true

case class BinNode[+A](elem: A, left: BinTree[A], right: BinTree[A]) extends BinTree[A] :
  val isEmpty : Boolean = false

object BinTree {

  def node[A](elem: A, left: BinTree[A], right: BinTree[A]) : BinNode[A] =
    BinNode(elem, left, right)

  val empty = EmptyTree
}
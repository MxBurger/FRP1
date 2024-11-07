package reduce

import java.util.concurrent.{ForkJoinPool, RecursiveTask}

trait ParReducible[A] extends Reducible[A] {
  val THRESHOLD = 7

  def size : Int
  def split: (ParReducible[A], ParReducible[A])

  def parReduceMap[B](mapper : A => B)(using monoid: Monoid[B]) : B = {

    class Task(parAs: ParReducible[A]) extends RecursiveTask[B] {
      override def compute(): B = ???
    }

    val task = new Task(this)
    ForkJoinPool.commonPool.invoke(task)
  }

  def parReduce(monoid: Monoid[A]) : A = parReduceMap(a => a)(using monoid)
}

object ParReducible {

  def apply[A](as: Iterable[A]) : ParReducible[A] =
    new ParReducible[A] {

      def size = as.size

      def split: (ParReducible[A], ParReducible[A]) = ???

      override def reduceMap[B](mapper: A => B)(using monoid: Monoid[B]): B = ???

      override def toString: String = as.toString()

    }

}

object ParReduceDemo {

  def main(args: Array[String]): Unit = {

    val ns = (1 to 100).toList

   val parReducible : ParReducible[Int] = ParReducible(ns)

    val sump = parReducible.parReduceMap( i => i )(using Monoid.intPlusMonoid)
    println(s"sump = $sump")

  }

}

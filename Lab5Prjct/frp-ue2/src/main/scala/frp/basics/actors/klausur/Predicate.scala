package frp.basics.actors.klausur

type Predicate[-T] = T => Boolean

extension[A] (p: Predicate[A])
  def &&(p2: Predicate[A]): Predicate[A] = a => p(a) && p2(a)
  def ||(p2: Predicate[A]): Predicate[A] = a => p(a) || p2(a)
  def not: Predicate[A] = a => !p(a)

def containsFn[A](a: A) : Predicate[List[A]] =
  lst => lst.contains(a)
def isEmptyFn[A] : Predicate[List[A]] =
  lst => lst.isEmpty
def notEmptyAndContainsFn[A](a: A) : Predicate[List[A]] =
  isEmptyFn.not && containsFn(a).not

@main
def PredicateMain(): Unit =
  println(notEmptyAndContainsFn(7)(List(1, 2, 3, 4, 5, 6, 8)))
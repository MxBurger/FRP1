package frp.basics.actors.klausur

trait Monoid[M] {
  def op(a: M, b: M): M

  val zero: M
}


object Monoid {
  def apply[M](z: M, operator: (M, M) => M): Monoid[M] =
    new Monoid[M] {
      override def op(a: M, b: M): M = operator.apply(a, b)

      override val zero: M = z
    }

  given intPlusMonoid: Monoid[Int] = Monoid(0, (x, y) => x + y)

  val intTimesMonoid: Monoid[Int] = Monoid(1, (x, y) => x * y)

  given doublePlusMonoid: Monoid[Double] = Monoid(0.0, (x, y) => x + y)

  val doubleTimesMonoid: Monoid[Double] = Monoid(1.0, (x, y) => x * y)

  given stringMonoid: Monoid[String] = Monoid("", (x, y) => x + y)

  given listMonoid[A]: Monoid[List[A]] = Monoid(List(), (l1, l2) => l1.appendedAll(l2))

  given setMonoid[A]: Monoid[Set[A]] = Monoid(Set(), (s1, s2) => s1 ++ s2) // union

  def reduceRight[A](as: List[A])(using monoid: Monoid[A]): A =
    as match {
      case Nil => monoid.zero
      case hd :: tl => monoid.op(hd, reduceRight(tl))
    }
  end reduceRight

  def optionMonoid[A](using elemMonoid: Monoid[A]): Monoid[Option[A]] =
    Monoid(None, (optA, optB) => {
      (optA, optB) match {
        case (None, None) => None
        case (Some(a), None) => optA
        case (None, Some(b)) => optB
        case (Some(a), Some(b)) => Some(elemMonoid.op(a, b))
      }
    })
  end optionMonoid
}

@main
def MonoidStuffMain(): Unit = {
  import Monoid.*

  val listOfOptions = List(Some(7), None, Some(2), None, Some(8), Some(6))
  given optionIntMonoid : Monoid[Option[Int]] = Monoid.optionMonoid
  val listOfOptionSum = reduceRight(listOfOptions)
  println(s"Sum of list of options: $listOfOptionSum")
}


package reduce

trait Monoid[M] {
  def op(a: M, b: M) : M
  val zero: M
}

object Monoid {

  def apply[M](z: M, operator: (M, M) => M): Monoid[M] =
    new Monoid[M] {
      override def op(a: M, b: M): M = operator.apply(a, b)
      override val zero: M = z
    }

  // TODO: Task 6.1 Monoid instances
  given intPlusMonoid: Monoid[Int] = ???
  val intTimesMonoid: Monoid[Int] = ???
  given doublePlusMonoid: Monoid[Double] = ???
  val doubleTimesMonoid: Monoid[Double] = ???
  given stringMonoid: Monoid[String] = ???
  def listMonoid[A]: Monoid[List[A]] = ???
  def setMonoid[A]: Monoid[Set[A]] = ???

  def optionMonoid[A](using elemMonoid: Monoid[A]) : Monoid[Option[A]] =
    Monoid(None, (optA, optB) => {
      (optA, optB) match {
        case (None, None) => None
        case (Some(a), None) => optA
        case (None, Some(b)) => optB
        case (Some(a), Some(b)) => Some(elemMonoid.op(a, b))
      }
    })

  def mapMonoid[K, V](using vMonoid: Monoid[V]) : Monoid[Map[K, V]] =
    Monoid(Map(), (mapA, mapB) => {
      var mapR = mapA
      for ((k, v) <- mapB) {
        if mapA.contains(k) then mapR = mapR.updated(k, vMonoid.op(v, mapA(k)))
        else mapR = mapR.updated(k, v)
      }
      mapR
    })
}
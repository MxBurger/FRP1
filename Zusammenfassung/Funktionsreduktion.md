### Monoide
Ein Monoid ist ein mathematisches Konzept aus der Kategorientheorie, das in der funktionalen Programmierung häufig verwendet wird.
Ein Monoid besteht aus drei Teilen:
- Einem Typ `M`
- Einer binären Operation (oft `combine` genannt, hier `op`), die zwei Werte vom Typ `M` kombiniert und einen neuen Wert vom Typ `M` zurückgibt
- Einem neutralen Element vom Typ `M` (oft `empty` genannt, hier `zero`)
  
Folgende Gesetze müssen erfüllt werden
- $(a 	\oplus b) 	\oplus c = a 	\oplus (b 	\oplus c)$
- Neutrales Element: $a \oplus empty \equiv empty \oplus a = a$

```scala
trait Monoid[M] {
  val zero: M
  def op(a: M, b: M) : M
}
```

```scala
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
}
```

Monoide lassen sich auch mit impliziten oder expliziten Kontext-Parametern in Scala verwenden.

```scala
def reduceRight[A](as: List[A])(using monoid: Monoid[A]): A =
    as match {
      case Nil => monoid.zero
      case hd :: tl => monoid.op(hd, reduceRight(tl))
    }
  end reduceRight
```

Das `given`-Schlüsselwort kann im Scope nur einmalig an den entsprechenden Datentyp vergeben werden. Der Compiler schaut nach givens:
- Im aktuellen Scope
- Givens durch imports (z.Bsp. `ExecutionContext`)
- Im Companion-Object der Klasse 

**Anwendung der erstellten Monoide**

```scala
val lst = List(1, 2, 3, 4, 5)

// given Monoid für Int
val sum1 = reduceRight(lst) // (intPlusMonoid) implicitly

// given Monoid für String
val str = reduceRight(lst.map(e => e.toString)) // (stringMonoid) implizit

// Monoid für Int explizit
val prod1 = reduceRight(lst)(using Monoid.intTimesMonoid) // (intTimesMonoid) explizit 

// a new custom given monoid for String
given customMonoid : Monoid[String] = Monoid("", (s1, s2) => s"$s1 $s2")
val str3 = reduceRight(lst.map(e => e.toString))  // (customMonoid) implizit
```

#### Erweiterung eines Monoids
Bei der Erweiterung eines Monoids müssen die grundlegenden Monoid-Gesetze weiterhin gelten.

**Beispiel mit `Optional`**

```scala
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
```
- Neutrales Element `zero` = `None`
- `combine`-Funktion `op` hat folgende Bedeutung
  - wenn beide Werte `None` -> `None`
  - wenn einer der beiden Werte `None` und der andere `Some(a)`, dann `Some(a)`
  - Wenn beide Werte `Some(a)` und `Some(b)` dann rufe die `combine`-Funktion (`op`) für die Elemente auf und retouniee es verpackt in einem `Some`

Explizite Context-Parameter
```scala
val listOfOptions = List(Some(7), None, Some(2), None, Some(8), Some(6))
val listOfOptionSum = reduceRight(listOfOptions)(using Monoid.optionMonoid(using Monoid.intPlusMonoid)) // Some(23)
```
Implizite Context-Parameter
```scala
val listOfOptions = List(Some(7), None, Some(2), None, Some(8), Some(6))
given optionIntMonoid : Monoid[Option[Int]] = Monoid.optionMonoid
val listOfOptionSum = reduceRight(listOfOptions) // Some(23)
```

---
### Parallelle Reduktion

Es gibt ein paar Grundkonzepte der parallelen Reduktion:
- Die Datenmenge wird in kleinere Teile aufgeteilt (split)
- Diese Teile werden parallel verarbeitet
- Die Teilergebnisse werden dann kombiniert (combine)

Für die Implementierung kann der der `ForkJoinPool` von Scala verwendet werden.

```scala
trait Reducible[A] {
  def reduceMap[B](mapper : A => B)(using monoid: Monoid[B]) : B
  def reduce(using monoid: Monoid[A]) : A = reduceMap( a => a )
}

trait ParReducible[A] extends Reducible[A] {
  val THRESHOLD = 7
  def size : Int
  def split: (ParReducible[A], ParReducible[A])
  def parReduceMap[B](mapper : A => B)(using monoid: Monoid[B]) : B = {
    class Task(parAs: ParReducible[A]) extends RecursiveTask[B] {
      override def compute() : B = {
        if (parAs.size <= THRESHOLD) then {
          parAs.reduceMap(mapper)
        } else {
          val (as1, as2) = parAs.split
          val task1 = new Task(as1)
          val task2 = new Task(as2)
          task1.fork()
          task2.fork()
          monoid.op(task1.join(), task2.join)
        }
      }
    }
    val task = new Task(this)
    ForkJoinPool.commonPool.invoke(task)
  }
  def parReduce(using Monoid[A]) : A = parReduceMap(a => a)
}

object ParReducible {
  def apply[A](as: Iterable[A]) : ParReducible[A] =
    new ParReducible[A] {
      def size = as.size
      def split: (ParReducible[A], ParReducible[A]) = {
        val (as1, as2) = as.splitAt(as.size / 2)
        (apply(as1), apply(as2))
      }
      override def reduceMap[B](mapper: A => B)(using monoid: Monoid[B]): B = {
        var b = monoid.zero
        for (a <- as) {
          b = monoid.op(b, mapper(a))
        }
        b
      }
      override def toString: String = as.toString()
    }
}
```

```scala
val ns = (1 to 50).toList
given m : Monoid[Int] = Monoid.intPlusMonoid
val sump = ParReducible(ns).parReduceMap( i => i )
println(s"Sum of list: $sump")
```
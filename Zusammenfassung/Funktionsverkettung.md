## Funktor

Eine Container-Struktur von Elementen des generischen Types `T`.
Die Haupteigenschaft eines Funktors ist die `map` Operation, die eine Funktion auf den eingekapselten Wert anwendet.
```scala
map : Functor[T] x (T => R) => Functor[R] 
```
Für `map` gilt folgendes:
- nimmt eine Funktion `f` als Parameter entgegen
- Diese Funktion auf den Inhalt des Funktors anwendet
- Einen neuen Funktor des gleichen Typs zurückgibt, aber mit transformierten Elementen (Funktor-Typ bleibt gleich, Element-Typ ist anders)

Ein Beispiel:
```scala
// List ist der Funktor-Typ
val numbers: List[Int] = List(1, 2, 3)
// map nimmt eine Funktion Int => String
val strings: List[String] = numbers.map(n => n.toString)
```
Ein paar bekannte Funktoren:
```scala
// List[A]
trait List[+A] :
  def map[B](f: A => B): List[B] = ...

// Option[A]
sealed abstract class Option[+A] :
  def map[B](f: A => B): Option[B] = ...

// Try[A]
trait Try[+T] :
  def map[R](fn: T => R) : Try[R] = ...
```
Alle haben Elemente eines generischen Types und eine `map`-Operation, welche auf den selben Typ mappt.

Funktor-Gesetze:

- Identitätsgesetz: `fa.map(x => x)` ist gleich `fa`
- Kompositionsgesetz: `fa.map(f).map(g)` ist gleich `fa.map(x => g(f(x)))`

Diese 2 Gesetze bewirken, dass die `map`-Operation die ursprüngliche Struktur des Containers beibehält.

`List` -> Die Ergebnis-Liste hat die selbe Anzahl an Elementen und die Elemente sind in der selben Reihenfolge.

`Òption` -> `None` wird gemapped auf `None`, `Some` wird gemapped auf `Some`



### Funktor

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


## Monaden

Ein Monad ist eine spezielle Art von Funktor mit zusätzlichen Möglichkeiten zur Komposition und Verkettung von Operationen.
**Struktur:**
   - Ein `Monad[A]` ist ein Container für Elemente vom Typ `A`
   - Ist ein `Funktor[A]` (alle Funktor-Eigenschaften gelten, (erbt alle Funktor-Eigenschaften))
   - Hat zwei wichtige zusätzliche Operationen: `unit` und `flatMap`

**Kern-Operationen**
  - `unit` nimmt einen Wert und verpackt ihn in den Monade
    ```scala
    // unit (auch "of" oder "pure" genannt)
    unit : A => Monad[A]  
    ```

  - `flatMap` verkettet monadische Operationen
      ```scala
      // flatMap (auch "bind" oder ">>=" genannt)
      flatMap : Monad[A] x (A => Monad[B]) => Monad[B]
      ```
Monaden erlauben das Verketten von Operationen (chaining). Jede Operation kann dabei den Kontext (den Monad) verändern. Diese Verkettung erfolgt durch die `flatMap`-Operation. Es gelten folgende Monaden-Gesetze:
- Identitätsgesetz: `of(x).flatMap(f) == f(x)`
- Assoziativitätsgesetz: `monad.flatMap(f).flatMap(g) == monad.flatMap(x => f(x).flatMap(g))`

**Scala-Beispiele**
```scala
// Option Monad
Some(x)        // unit
def flatMap[B](f: A => Option[B]): Option[B]

// Try Monad
Success(r)     // unit
Failure(e)     // unit für Fehlerfall
def flatMap[U](f: T => Try[U]): Try[U]

// List/Iterable Monad
List(elem)     // unit
def flatMap[B](f: A => Iterable[B]): Iterable[B]
```

Monaden bieten einen Berechnungen zu Verketten, die in einem bestimmten Kontext stattfinden. (z.Bsp.: Fehlerbehandlung in der `Try`-Monade oder das mögliche abwesend sein von Elementen bei `Optional`)

**Beispiel-Anwendung der Optional-Monade**
```scala
val roomOpt = for {
  id <- stds.get("Hans")        // Könnte None sein
  course <- courses.get(id)     // Könnte None sein
  room <- rooms.get(course)     // Könnte None sein
} yield room
```
*Äquivalenter Aufruf:*
```scala
val roomOpt = stds.get("Hans").
  flatMap(id => courses.get(id)).
  flatMap(course => rooms.get(course)).
  filter(room.startsWith("HS"))
```
Hier ist der "Kontext" die mögliche Abwesenheit eines Wertes. Wenn irgendeiner der Schritte None zurückgibt, wird automatisch None weitergegeben. Wir müssen nicht jeden Schritt mit `if-else` prüfen.

**Beispiel-Anwendung der Try-Monade**
```scala
val result = for {
  data <- Try(readFile())          // Könnte fehlschlagen
  parsed <- Try(parseData(data))   // Könnte fehlschlagen
  processed <- Try(process(parsed)) // Könnte fehlschlagen
} yield processed
```
Der "Kontext" hier ist die mögliche Fehlerbehandlung. Wenn irgendein Schritt fehlschlägt, wird der Fehler automatisch weitergegeben.

**Beispiel-Anwendung der List-Monade**
```scala
// Eine einfache Liste von Zahlen
val numbers = List(1, 2, 3, 4)

// Wir wollen für jede Zahl ihre Teiler finden
val divisors = for {
  n <- numbers           // Für jede Zahl in der Liste
  d <- 1 to n           // Für jede mögliche Zahl von 1 bis n
  if n % d == 0         // Nur wenn es ein Teiler ist
} yield (n, d)

// Ergebnis:
// List(
//   (1,1),
//   (2,1), (2,2),
//   (3,1), (3,3),
//   (4,1), (4,2), (4,4)
// )
```
Der "Kontext" hier ist die Verarbeitung mehrerer Werte. Die Monade kümmert sich darum, alle möglichen Kombinationen zu erzeugen.

## Funktions-Komposition
Function Composition ist ein zentrales Konzept der funktionalen Programmierung in Scala, bei dem Funktionen als Bausteine verwendet werden, um komplexere Funktionen zu erstellen. **Function Composition Operatoren** sind selbst Funktionen, die andere Funktionen als Parameter akzeptieren. Sie geben wiederum Funktionen als Ergebnis zurück. Das ermöglicht das Erstellen komplexerer Funktionen aus einfacheren Funktionen.

```scala
trait Function1[-T1, +R] extends AnyRef { self =>
  def apply(v1: T1): R

  //Wendet erst die übergebene Funktion g an und dann die aktuelle Funktion
  def compose[A](g: A => T1): A => R = { x => this.apply(g(x)) }

  // Wendet erst die aktuelle Funktion an und dann die übergebene Funktion g
  def andThen[A](g: R => A): T1 => A = { x => g(this.apply(x)) }
}
```

Beispiel aus den Folien:
```scala
val wordsFn: String => List[String] = 
  line => line.split(" ").toList
val sortFn: List[String] => List[String] = 
  words => words.sorted
val wordsSortFn: String => List[String] = 
  wordsFn.andThen(sortFn)

println(wordsSortFn("functional programming is great"))
// Ausgabe: List(functional, great, is, programming)
```

\
**Exkurs: Erweiterungs-Methoden (Extension-Methods)**
```scala
extension (s: String)
  def toInt: Int = Integer.parseInt(s)
  def toLong: Long = java.lang.Long.parseLong(s)
  def toDouble: Double = java.lang.Double.parseDouble(s)

// Verwendung
"123".toInt
"0.5".toDouble
```
```scala
object ListOps :
  extension[T](l : List[T])
    def isLong : Boolean = l.size > 10
    def isShort : Boolean = l.size <= 10

// Verwendung
import ListOps.*
val lst = List(1, 2, 3, 4)
if (lst.isLong) then ... 
```

Für die Kombination von Prädikaten können eigene Kompositions-Operatoren erstellt werden.

Sie werden folgendermaßen definiert.
```scala
type Predicate[-T] = T => Boolean

extension[A] (p: Predicate[A])
  def &&(p2: Predicate[A]): Predicate[A] = a => p(a) && p2(a)  
  def ||(p2: Predicate[A]): Predicate[A] = a => p(a) || p2(a)
  def not: Predicate[A] = a => !p(a)
```

Und kommen hier zur Anwendung.

*Einfaches Beispiel:*
```scala
// Einzelne Prädikate
val isPositive: Predicate[Int] = x => x > 0
val isEven: Predicate[Int] = x => x % 2 == 0

// Kombinierte Prädikate
val isPositiveAndEven = isPositive && isEven
val isPositiveOrEven = isPositive || isEven
val isNotPositive = isPositive.not

// Anwendung
val numbers = List(1, 2, -3, 4, -5, 6)
numbers.filter(isPositiveAndEven) // Ergibt: List(2, 4, 6)
```

*Komplexeres Beispiel:*
```scala
def containsFn[A](a: A) : Predicate[List[A]] =
lst => lst.contains(a)

def isEmptyFn[A] : Predicate[List[A]] =
lst => lst.isEmpty

def notEmptyAndContainsFn[A](a: A) : Predicate[List[A]] =
isEmptyFn.not && containsFn(a).not

println(notEmptyAndContainsFn(7)(List(1, 2, 3, 4, 5, 6, 8))) // true
```
Die zweite Parameterliste entsteht durch Currying. Eine Funktion nicht alle Parameter auf einmal, sondern sie werden schrittweise angewendet. Die ausgeschriebene Typ-Inferenz würde so aussehen:
```scala
// Typ: Int => (List[Int] => Boolean)
val checkFor7 = notEmptyAndContainsFn(7) 

// Typ: Boolean
val result = checkFor7(List(1, 2, 3, 4, 5, 6, 8))
```

### Currying :curry:

Currying (kA was das auf deutsch ist :smile:) ist ein fundamentales Konzept in der funktionalen Programmierung.

**Funktion ohne Currying:**
```scala
def add(x: Int, y: Int): Int = x + y
// Aufruf:
add(2, 3) // Ergebnis: 5
```
**Selbe Funktion "gecurried":**
```scala
def addCurried(x: Int)(y: Int): Int = x + y
// Aufruf:
addCurried(2)(3) // Ergebnis: 5
```
Das bietet die Möglichkeit, die Funktion teilweise anzuwenden.
```scala
// Erstellt eine neue Funktion, die 2 zu einer Zahl addiert
val add2 = addCurried(2)
// add2 ist jetzt vom Typ: Int => Int

// Kann mehrfach verwendet werden:
add2(3) // Ergebnis: 5
add2(4) // Ergebnis: 6
add2(5) // Ergebnis: 7
```
Currying ist besonders nützlich, wenn wir:
- Funktionen teilweise mit Parametern "vorkonfigurieren" möchten
- eine Funktion als Parameter an andere Funktionen übergeben möchten
- Funktionen komponieren möchten


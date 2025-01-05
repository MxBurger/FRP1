## Scala-Stuff zum Merken

### `map`
`map` ist eine fundamentale Operation, die auf verschiedenen Datenstrukturen in Scala angewendet werden kann. Im Kern macht `map` Folgendes:
- Es nimmt eine Funktion als Parameter, die eine Transformation beschreibt
- Es wendet diese Funktion auf jedes Element in der Datenstruktur an
- Es behält dabei die Struktur des ursprünglichen Containers bei

```scala
// Liste
val numbers = List(1, 2, 3)
numbers.map(x => x * 2)  // Ergebnis: List(2, 4, 6)

// Option
val maybeNumber = Some(4)
maybeNumber.map(x => x * 2)  // Ergebnis: Some(8)

// Array
val arr = Array("a", "b", "c")
arr.map(s => s.toUpperCase)  // Ergebnis: Array("A", "B", "C")

// Set
val set = Set(1, 2, 3)
set.map(x => x + 10)  // Ergebnis: Set(11, 12, 13)
```
Wichtige Eigenschaften:
- Die Containerart bleibt erhalten (Liste bleibt Liste, Option bleibt Option, etc.)
- Die Anzahl der Elemente bleibt gleich (1-zu-1-Transformation)
- Die Transformation ist unabhängig für jedes Element
- Der Rückgabetyp der Transformation kann sich aber vom ursprünglichen Typ unterscheiden

  ```scala
  val numbers = List(1, 2, 3)
  numbers.map(x => x.toString)  // List[Int] wird zu List[String]
  ```

--- 

### `flatMap`

`map` und `flatMap` sind besonders im  Kontext von Monaden wichtig.

**Erinnerung `map`:**
  ```scala
  // Grundlegende Syntax
  option.map(f: A => B): Option[B]

  // Beispiel
  Some(3).map(x => x * 2) // Ergebnis: Some(6)
  None.map(x => x * 2)    // Ergebnis: None
  ```
  `map` transformiert den Wert innerhalb eines `Option`-Containers, ohne die "Container-Ebene" zu verändern. Wenn das `Option` `None` ist, bleibt es `None`.


**Der Unterschied durch `flatMap`:**
`flatMap` unterscheidet sich von `map` dadurch, dass die übergebene Funktion bereits ein `Option` zurückgibt. `flatMap` "flacht" dann das Ergebnis ab, sodass nicht ein `Option[Option[B]]` entsteht.

```scala
// Grundlegende Syntax
option.flatMap(f: A => Option[B]): Option[B]

// Beispiel
Some(3).flatMap(x => Some(x * 2))     // Ergebnis: Some(6) und nicht Some(Some(6))
Some(3).flatMap(x => None)            // Ergebnis: None und nicht Some(None)
None.flatMap(x => Some(x * 2))        // Ergebnis: None
```
**Beispiel:**
```scala
case class User(name: String)
case class Address(street: String)

def findUser(id: Int): Option[User] = ???
def findAddress(user: User): Option[Address] = ???

// Mit map (führt zu verschachtelten Options)
findUser(123).map(user => findAddress(user)) 
// Ergebnis: Option[Option[Address]]

// Mit flatMap (flacht das Ergebnis ab)
findUser(123).flatMap(user => findAddress(user)) 
// Ergebnis: Option[Address]
```
Das ist nützlich für:
- Verkettung von Operationen, die optional fehlschlagen können
- Vermeidung von tief verschachtelten `Option`-Typen
- Implementierung der for-comprehension Syntax



`flatMap` geht auch für andere ContainerTypen in Scala, nicht nur für `Option`:
*Beispiel mit Liste:*
```scala
// Mit map - erzeugt verschachtelte Listen
List(1, 2, 3).map(x => List(x, x * 2))
// Ergebnis: List(List(1, 2), List(2, 4), List(3, 6))

// Mit flatMap - flacht die Listen ab
List(1, 2, 3).flatMap(x => List(x, x * 2))
// Ergebnis: List(1, 2, 2, 4, 3, 6)
```

---

### `reduce`
`reduce` ist eine Operation zum "Zusammenfalten" einer Collection in einen einzelnen Wert. Dabei wird eine binäre Operation (eine Funktion, die zwei Werte kombiniert) auf alle Elemente nacheinander angewendet. `a` immer das "akkumulierte" Ergebnis der vorherigen Operationen, und `b` ist das nächste Element aus der Liste.
- Die Collection darf nicht leer sein, sonst gibt es eine Exception
- Der Rückgabetyp muss der gleiche sein wie der Typ der Elemente
- Die Operation sollte assoziativ sein für vorhersagbare Ergebnisse
  
```scala
// Summe aller Zahlen
List(1, 2, 3, 4).reduce((a, b) => a + b)  // Ergebnis: 10

// Das Gleiche kürzer geschrieben
List(1, 2, 3, 4).reduce(_ + _)  // Ergebnis: 10

// Multiplikation aller Zahlen
List(1, 2, 3, 4).reduce(_ * _)  // Ergebnis: 24

// Größte Zahl finden
List(4, 2, 7, 1).reduce((a, b) => if (a > b) a else b)  // Ergebnis: 7
```
`reduce` wird von links nach rechts auf die Elemente angewendet.
```scala
List(1, 2, 3, 4).reduce(_ + _)

// Wird ausgeführt als:
((1 + 2) + 3) + 4

// Schrittweise:
//1. 1 + 2 = 3
//2. 3 + 3 = 6
//3. 6 + 4 = 10
```

Will man explizit die Richtung festlegen, gibt es auch:

- `reduceLeft`: garantiert von links nach rechts (wie reduce)
- `reduceRight`: von rechts nach links

```scala
// reduceRight
List(1, 2, 3, 4).reduceRight(_ + _)
// Wird ausgeführt als:
1 + (2 + (3 + 4))
```

Bei manchen Operationen wie `+` oder `*` macht die Reihenfolge keinen Unterschied, weil sie assoziativ sind. Bei nicht-assoziativen Operationen wie `-` oder `/` kann die Reihenfolge aber zu unterschiedlichen Ergebnissen führen:
```scala
List(1, 2, 3).reduce(_ - _)     // ((1 - 2) - 3) = -4
List(1, 2, 3).reduceRight(_ - _) // (1 - (2 - 3)) = 2
```

--- 

### `fold`
`fold` ist ähnlich wie `reduce`, aber mit einem wichtigen Unterschied: Es nimmt einen Startwert ("neutral element").

Syntax:
```scala
collection.fold(neutral)((accumulator, element) => /* Operation */)
```
Beispiele:
```scala
// Mit Zahlen
List(1, 2, 3).fold(0)(_ + _)  // Startwert 0, Ergebnis: 6
List(1, 2, 3).fold(10)(_ + _) // Startwert 10, Ergebnis: 16

// Mit Strings
List("a", "b", "c").fold("iAmACat ")(_ + _)  // Ergebnis: "iAmACat abc"

// Funktioniert auch mit leeren Listen (anders als reduce!)
List[Int]().fold(0)(_ + _)  // Ergebnis: 0
```

Es gibt auch `foldLeft` und `foldRight`, analog zu `reduce`:
```scala
// foldLeft geht von links nach rechts
List(1, 2, 3).foldLeft(0)(_ - _)    // ((0 - 1) - 2) - 3 = -6

// foldRight geht von rechts nach links
List(1, 2, 3).foldRight(0)(_ - _)   // 1 - (2 - (3 - 0)) = 2
```

Die Hauptvorteile von fold gegenüber reduce:
- Funktioniert mit leeren Collections
- Der Ergebnistyp kann sich vom Elementtyp unterscheiden
- Man kann einen sinnvollen Startwert angeben

Beispiele mit unterschiedlichen Rückgabetypen als Elementtypen:
```scala
// Liste von Zahlen zu String
List(1, 2, 3).fold("")((acc, num) => acc + num.toString)
// Ergebnis: "123"
// Elementtyp: Int, Ergebnistyp: String

// Liste von Strings zu Zahl (Anzahl der Zeichen)
List("hello", "world").fold(0)((acc, str) => acc + str.length)
// Ergebnis: 10
// Elementtyp: String, Ergebnistyp: Int

// Liste zu Map (z.B. Häufigkeitszählung)
List("a", "b", "a", "c").fold(Map[String, Int]()) { (acc, str) =>
  acc + (str -> (acc.getOrElse(str, 0) + 1))
}
// Ergebnis: Map("a" -> 2, "b" -> 1, "c" -> 1)
// Elementtyp: String, Ergebnistyp: Map[String, Int]
```
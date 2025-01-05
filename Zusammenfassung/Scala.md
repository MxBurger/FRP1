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
## Non-strict Evaluation
Strikte Auswertung bedeutet *call-by-value*:
- Ausdrücke für Parameter in Methodenaufrufen werden ausgewertet
- Danach wird dieser Wert im Methodenaufruf übergeben


Nicht-strikte Auswertung bedeutet *call-by-name*:
- Ausdrücke für Parameter in Methodenaufrufen werden nicht ausgewertet
- Stattdessen werden sie als nicht ausgewertete Ausdrücke übergeben


Bei nicht-strikter Auswertung können Ausdrücke ausgewertet werden:
- In einem spezifischen Kontext
  - Der Ausdruck wird erst dann ausgewertet, wenn er in einem bestimmten Programmabschnitt oder unter bestimmten Bedingungen benötigt wird
  - Beispiel: `unless` ... hier wird der Code-Block nur in einem spezifischen Kontext (wenn die Bedingung false ist) ausgewertet
- Verzögert nach Bedarf, d.h. wenn der Wert wirklich benötigt wird

Nicht-strikte Auswertung kann erreicht werden durch:
- *by-name* Parameter (Scala)
- Funktion als Parameter
- mit `lazy` Werten

## by-name Parameter
- Tatsächliche Argument-Ausdrücke werden unausgewertet übergeben
- Der Parameter im Methodenrumpf wird durch den Ausdruck ersetzt
- by-name Parameter sind keine Funktionsobjekte (keine First-Class Objekte)
- Nur die Parameter-Übergabe erfolgt by-name
  - Innerhalb der Methode wird der Ausdruck jedes Mal neu ausgewertet, wenn der Parameter verwendet wird. 
```scala
def method[T](param : => T) = {
.... context ...
param
.... context ...
}
```

Der wesentliche Unterschied zu normalen Parametern ist, dass der übergebene Ausdruck erst dann ausgewertet wird, wenn er im Methodenrumpf tatsächlich verwendet wird. Dabei wird der Parameter-Name einfach durch den ursprünglichen Ausdruck ersetzt, ohne ein Funktionsobjekt zu erstellen.

**Beispiel `unless`:**
```scala
def unless[A](cond: Boolean) (code : => A) = {
    if (! cond) {
        code
    }
}
```
Anwendung:
```scala
val y = 10
val x = 0

unless(x == 0) {
    println(y / x)
}

// wird zu:
if (! true) {
    println(y / x)
}
```
- `cond` wird durch *call-by-value* ausgewertet
- `code` wird durch *call-by-name* übergeben
- Der nicht ausgewertete Code wird in den Methodenrumpf eingebettet
- Die Auswertung erfolgt durch Ersetzung

Der generische Typ `A` wird verwendet, um die Methode `unless` flexibler und wiederverwendbarer zu machen.

Ohne generischen Typ müsste man sich auf einen spezifischen Rückgabetyp festlegen, z.B.:
```scala
def unless(cond: Boolean)(code: => Unit)  // Nur für Ausdrücke ohne Rückgabewert
// oder
def unless(cond: Boolean)(code: => Int)   // Nur für Ausdrücke die Int zurückgeben
// oder
def unless(cond: Boolean)(code: => String) // Nur für Ausdrücke die String zurückgeben
```

Mit dem generischen Typen `A` kann die Methode mit jedem beliebigen Rückgabetyp verwendet werden.
```scala
// Mit Unit als Rückgabetyp
unless(x == 0) {
    println("Hello")  // Rückgabetyp ist Unit
}

// Mit Int als Rückgabetyp
val result = unless(x == 0) {
    42  // Rückgabetyp ist Int
}

// Mit String als Rückgabetyp
val greeting = unless(x == 0) {
    "Hello"  // Rückgabetyp ist String
}
```

## lazy Vals
Das Schlüsselwort `lazy` für `val`-Variablen bedeutet, dass die Initialisierung verzögert wird, bis die Variable zum ersten Mal verwendet wird.
```scala
lazy val lazyX = initialization-expression  // wird initialisiert bei erstem Zugriff
```
Anwendungen:
- Verzögern der Ausführung von Initialisierungscode, bis der Wert wirklich benötigt wird
- Vermeiden der Ausführung von Initialisierungscode, wenn der Wert nie benötigt wird
- einmalige Auswertung (das Ergebnis wird nach der ersten Auswertung gespeichert)

**Beispiel:**
```scala
val value = {
  println("setting value")
  "-- value of val --"
}

lazy val lazyVal = {
  println("setting lazy val")
  "-- value of lazy val --"
}

def function = {
  println("calling function")
  "-- value of function --"
}

println()
println(function)
println(lazyVal)
println(value)
println()
println(function)
println(lazyVal)
println(value)
println()
```

führt zu folgender Ausgabe:
```
setting value
calling function
-- value of function --
setting lazy val
-- value of lazy val --
-- value of val --
calling function
-- value of function --
-- value of lazy val --
-- value of val --
```

> [REMINDER] Auch Blöcke sind in Scala Ausdrücke. Der Wert eines Blocks ist der Wert des letzten Ausdrucks im Block.

## lazy Sequences am Beispiel von Streams

Streams sind verzögert ausgewertete (`lazy`) Sequenzen von Elementen:
- verwendet Thunks zur Darstellung der Element-Generierung
- Elemente werden nur bei Bedarf (= bei Zugriff) erzeugt

```scala
val largeColl = List(10, 20, 7, 33, -15, 6, 9, 2, ...)
val stream = largeColl.toStream           // stream : Stream[Int] = Stream(10, ?)
val tlStream = stream.tail                // tlStream : Stream[Int] = Stream(20, ?)

val someElems = stream.drop(2).take(3).filter(_ > 0).toList 
// someElems : List[Int] = List(7, 33, 6)
```

Das ? in der Stream-Darstellung bedeutet "nicht ausgewertet = thunk!". Dies zeigt, dass die nachfolgenden Elemente erst bei Bedarf berechnet werden. Ein "Thunk" ist ein Konzept, das einen verzögert ausgewerteten Berechnungsausdruck beschreibt. `?` bedeutet, dass dort ein Thunk ist, der die Berechnung für die restlichen Elemente enthält.

- Ein Stream wandelt eine Liste in eine lazy Sequenz um
- Nur die angeforderten Elemente werden tatsächlich berechnet
- Operationen können verkettet werden, bevor die eigentliche Berechnung stattfindet
- Die tatsächliche Auswertung erfolgt erst bei der Umwandlung in eine Liste (`toList`) (Materialisierung)

**Beispiel:**

```scala
val largeColl = List(10, 20, 7, 33, -15, 6, 9, 2, ...)
// ? = thunk
val stream = largeColl.toStream // stream : Stream[Int] = Stream(10, ?)

val tlStream = stream.tail // tlStream : Stream[Int] = Stream(20, ?)

// elements werden nur bei Bedarf generiert (beim Zugriff)
val someElems = stream.drop(2).take(3).filter(_ > 0).toList // Materialisierung durch .toList
// someElems : List[Int] = List(7, 33, 6)
```

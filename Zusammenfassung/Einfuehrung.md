## Einführung in funktionale Programmierung

Funktionale Programmierung **(FP)** ist Programmierung mit **mathematischen Funktionen**.

```math
x=y \Rightarrow f(x) = f(y)
```
Ergebnisse sind nur von den Werten der Input-Argumente abhängig -> keine Seiteneffekte.

### Reine (Pure) funktionale Programme
bestehen ausschließlich aus:
- **Werten (Values)** und **Typen (Types)**
- **Funktionsdefinitionen**
  - auch rekursive Funktionen
- **Ausdrücke** die die Funktionen anwenden (**Function Application Expressions**)
- **Funktionen höherer Ordnung (high-order functions)** d.h. Funktionen, die Funktionen als Input-Parameter haben oder eine Funktion als Rückgabewert liefern
- **Funktionskomposition** d.h. neue Funktionen durch das Kombinieren von Funktionen erstellen

### Unterschiede zu imperativer und Objekt-Orientierter-Programmierung

- Es gibt keinen Wertespeicher (*value store*)
Ein "Value Store" oder Wertspeicher bezieht sich auf den Bereich im Speicher, in dem Variablen und ihre Werte während der Programmausführung gespeichert werden. In imperativen und objektorientierten Sprachen ist das natürlich ein zentrales Konzept.
  *Beispiel C#:*
  ```csharp
  int x = 5;  // Speichert 5 im Wertspeicher
  x = x + 1;  // Ändert den Wert im Speicher zu 6
  ``` 
  *Beispiel Scala:*
  ```scala
  val x = 5  // Keine Variable, sondern eine unveränderliche Bindung
  val y = x + 1  // Erzeugt einen neuen Wert, ändert nicht x
  ```
  In der funktionalen Programmierung denkt man nicht in Speicherzuständen und deren Veränderungen, sondern in Transformationen von Werten durch Funktionen.

- Keine Zeiger oder Referenzen
- Keine Zuweisungen oder Änderungen im Speicher
- Keine Seiteneffekte
- Keine Anweisungen oder Anweisungssequenzen
- Stattdessen nur Ausdrücke

### Programmierung ausschließlich mit Funktionen: 
  - theoretisch möglich (Lambda-Kalkül)
  - praktisch nützlich und sogar relevant (es gibt reine Programmiersprachen, z.Bsp. Haskell :vomiting_face:)

### Imperative vs Funktionale Programmierung
**Beispiel: Inneres Produkt zweier Vektoren $a$ und $b$**

#### Imperative Lösung
Diese Lösung ist charakterisiert durch:
- Veränderliche Zustände (mutable state)
- Verarbeitung einzelner Elemente nacheinander ("Wort-für-Wort-Abarbeitung")
```java
int c = 0;
for (int i = 0; i < n; i++) {
    c = c + a.get(i) * b.get(i);
}
```

#### Funktionale Programmierung
Diese Lösung ist charakterisiert durch: 
- Aggregierte Operationen, die auf den gesamten Vektoren arbeiten und nicht auf einzelnen Elementen
- Funktionen nehmen andere Funktionen als Parameter *(high order functions)*
- Funktionen werden zu komplexeren Funktionen zusammengesetzt *(function composition)*

```scala
int c = map2((x, y) -> x * y).andThen(reduce((r, x) -> r + x))(a, b)
```

### Vorteile von Funktionaler Programmierung
- Funktionales Programmierung arbeitet auf einer höheren, deklarativeren Abstraktionsebene
- Funktionale Programme sind weniger Fehleranfällig und robuster
- Funktionale Programme können leichter verifiziert/überprüft werden
*(bedeutet hier die formale Verifikation oder Überprüfung der Korrektheit eines Programms. Funktionale Programme sind einfacher zu verifizieren aus mehreren Gründen:)*
  - Mathematische Beweisbarkeit:
    - Funktionale Programme verhalten sich wie mathematische Funktionen
    - Für den gleichen Input liefern sie immer den gleichen Output
    - Dies macht es möglich, mathematische Beweise über das Programmverhalten zu führen
  - Keine Seiteneffekte:
    - Da funktionale Programme keine Seiteneffekte haben, müssen bei der Verifikation keine globalen Zustände oder versteckte Änderungen berücksichtigt werden
    - Man muss nur den Input und Output einer Funktion betrachten
  - Modularität:
    - Jede Funktion kann isoliert getestet und verifiziert werden
    - Das Verhalten einer Funktion hängt nur von ihren Parametern ab, nicht von externen Zuständen
  - Unveränderlichkeit (Immutability):
    - Da Daten nicht verändert werden können, gibt es weniger mögliche Fehlerzustände
    - Man muss nicht verschiedene Zeitpunkte und Zustände von Variablen berücksichtigen
- Funktionale Programme eignen sich gut für parallele Ausführung, weil:
  - die Ausführungsreihenfolge von Ausdrücken für das Ergebnis irrelevant ist
- Funktionale Programmierungsprinzipien sind vielversprechend für nebenläufige und verteilte Anwendungen, z.Bsp.:
  - Twitter message server
  - WhatsApp Kernel
> Dies gilt für funktionale Programmierung ohne Seiteneffekte!

### Konzepte der Funktionalen Programmierung


| Konzept | Unterstützt in Java | Unterstützt in Scala |
|---------|-------------------|-------------------|
| Programmierung mit Funktionen | Ja | Ja |
| Funktionsobjekte und Funktionen höherer Ordnung | Ja | Ja |
| Typ-Inferenz | Teilweise | Ja, nicht vollständig |
| Algebraische Datentypen* | Ja | Ja |
| Polymorphe Datentypen (Generics) | Ja, für Referenztypen | Ja |
| Pattern Matching Ausdrücke | Ja | Ja |
| Nicht-strikte Auswertung (Lazy Evaluation)** | für Streams | Ja |
| Monaden für Funktionskomposition | Ja | Ja |
| Monad Comprehension | Nein | Ja |

__\*__  Algebraische Datentypen (ADTs) sind ein Konzept, bei dem komplexe Datentypen aus einfacheren Datentypen durch zwei Hauptoperationen zusammengesetzt werden:

- Produkt-Typen **(AND)**:
Kombinieren mehrere Typen zu einem neuen Typ
```scala
case class Person(name: String, age: Int)
```
Summen-Typen **(OR)**:
- Repräsentieren Alternativen zwischen verschiedenen Typen
```scala
sealed trait Shape
case class Circle(radius: Double) extends Shape
case class Rectangle(width: Double, height: Double) extends Shape
```
Algebraische Datentypen punkten mit:
- Typsicherheit
- Durch Pattern Matching vollständig überprüfbar
- Können unmögliche Zustände durch das Typsystem ausschließen

__\**__  Nicht-strikte Auswertung (auch faule oder lazy Auswertung genannt) bedeutet, dass Ausdrücke erst dann ausgewertet werden, wenn ihr Wert tatsächlich benötigt wird
```scala
// Strikte (eager) Auswertung
val x = {
  println("Berechne x")
  42
}
// "Berechne x" wird sofort ausgegeben

// Nicht-strikte (lazy) Auswertung
lazy val y = {
  println("Berechne y")
  42
}
// Nichts wird ausgegeben
// Erst wenn y verwendet wird:
println(y) // Jetzt wird "Berechne y" und dann 42 ausgegeben
```
Besser ist das ersichtlich bei Streams oder Sequenzen:
```scala
// Eager/Strikte Evaluation würde sofort eine riesige Liste erstellen
val numbers = List.range(1, 1000000000)

// Lazy Evaluation erstellt die Zahlen erst wenn sie gebraucht werden
val lazyNumbers = Stream.range(1, 1000000000)
// Erst wenn wir z.B. die ersten 5 Elemente wollen:
lazyNumbers.take(5).foreach(println)
```
Lazy Evaluation ermöglicht:
- Bessere Performance durch vermiedene unnötige Berechnungen
- Möglichkeit, mit unendlichen Sequenzen zu arbeiten
- Effizientere Speichernutzung

Java-Beispiel mit Streams:
```java
// Lazy - wird nicht sofort ausgewertet
Stream<Integer> numbers = Stream.iterate(1, n -> n + 1);
// Erst hier werden die ersten 5 Zahlen tatsächlich berechnet
numbers.limit(5).forEach(System.out::println);
```

### Reine vs Unreine :smile: Funktionen 

- Reine Funktionen = retournieren ausschließlich Werte
  - keine Seiteneffekte
  - gleiche Argumente -> gleiche Ergebnis-Werte
  - keine Abhängigkeite zu einem (veränderbaren) Zustand
  - alles was sich auf das Ergebnis auswirken soll, muss als Argument übergeben werden
- Unreine :smile: Funktionen = Funktionen mit Seiteneffekten
  - der selbe Aufruf mit selben Argumenten hintereinander kann verschiedene Ergebnisse liefern
  - kann von einem (externen Zustand) abhängig sein 

### Beispiel Zufallszahlen-Generator

**Zufallszahlengenerierung mit `java.util.Random`**
- Gleicher Funktionsaufruf, unterschiedliche Ergebnisse → `nextInt` ist keine Funktion *(im Sinne der funktionalen Programmierung)*
- Die Funktion `nextInt` ist zustandsbehaftet
  ```scala
  val random = java.util.Random(977)
  val rn1 = random.nextInt()  // rn1 = -1223585132
  val rn2 = random.nextInt()  // rn2 = 1933351633  unterschiedliche   Ergebnisse!
  ```

**Zufallszahlengenerierung mit purer Funktion**
Eine reine Funktion verwendet nur Parameter und Rückgabewerte und hat keinen Zustand und keine Seiteneffekte. 
- Die Funktion `rand` nimmt einen Startwert (Seed) und gibt einen Zufallswert plus einen neuen Seed zurück

  ```scala
  def rand(seed: Long) : (Int, Long) = {
      val i = (seed >>> 16).toInt
      val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
      (i, newSeed)
  }

  val r1 = rand(12312341977L)     // (187871,5530422524784)
  val r2 = rand(12312341977L)     // (187871,5530422524784)

  val s0 = 12312341977L
  val (i1, s1) = rand(s0)         // (187871,5530422524784)
  val (i2, s2) = rand(s1)         // (84387550,186084096765627)
  ```

### Vorteile von reinen Funktionen

- Parallele Ausführung
  - Ausdrücke können parallel ausgeführt werden
- Lazy Evaluation (bedarfsgesteuerte *(on-demand)* Ausführung)
  - Ausdrücke können ausgewertet werden, wenn das Ergebnis benötigt wird
- Testbarkeit
  - Reine Funktionen sind unabhängig von anderen und können unabhängig getestet werden
- Komponierbarkeit
  - Funktionen sind komponierbar (lassen sich zusammensetzen)
- Memoization (Zwischenspeicherung)
  - Sobald ein Wert für einen bestimmten Argumentwert berechnet wurde, kann er für die spätere Verwendung zwischengespeichert *(cached)* werden

### Schleifen vs. rekursive Funktionen

Funktionen mit Schleifen verändern Variablen und haben daher Seiteneffekte.

#### Beispiel Fakultät:
**Imperative Lösung mit veränderbaren Variablen `f` und `i`**
```scala
def facIter(n: Int) : Int = {
    var f = 1
    for (i <- 2 to n) {
        f = f * i
    }
    f
}
```
Die rekursive Lösung hat keine veränderbare Variable
```scala
def facRec(n: Int) : Int = {
    if (n == 1) then 1
    else n * facRec(n - 1)
}
```

> Von außen betrachtet sind beide reine Funktionen, da die veränderbaren Variablen `f` und `i` lokal sind und keine nach außen sichtbaren Seiteneffekte haben
>[INFO] lokale veränderbare Variablen werden in Scala häufig aus Performancegründen verwendet

### Referenzielle Transparenz

Eine wichtige Eigenschaft eines Ausdrucks *(Expression)* ist die Referenzielle Transparenz.
Ein Ausdruck ist referenziell transparent, wenn der Wert eines Ausdrucks, der Teilausdrücke enthält, **nur** von den Werten der Teilausdrücke **abhängig** ist!
Alle anderen Eigenschaften wie:
- interne Struktur des Ausdrucks
- Anzahl und Art seiner Komponenten
- Reihenfolge der Auswertung

sind irrelevant.

Die Ausdrücke können ausgewertet werden:
...von links nach rechts
...von rechts nach links
... parallel
```scala
val r = facIter(5) - facIter(7)     // referenziell transparent

val r2 = random.nextInt() - random.nextInt();    // nicht referenziell transparent
```
:exclamation:Bei referenzieller Transparenz spielt es keine Rolle, wie oder in welcher Reihenfolge die Berechnung durchgeführt wird - das Ergebnis ist immer dasselbe, solange die Eingabewerte gleich bleiben.

### Anmerkungen zu Funktionaler Programmierung in Scala

- Scala ist eine rein objektorientierte Programmiersprache
- Scala läuft auf der Java VM
  - welche keine funktionale Ausführungsumgebung ist :smile:
- nicht rein funktional
  - erlaubt Seiteneffekte

Aber ...
- Scala hat eine vollständige Implementierung funktionaler Prinzipien (von Haskell)
- Scala ermöglicht rein funktionale Programmierung
- Scala-Bibliotheken sind (hauptsächlich) auf funktionalen Prinzipien aufgebaut
  - oft mit funktionalem Interface nach außen und imperativer Implementierung im Inneren (aus Performancegründen)



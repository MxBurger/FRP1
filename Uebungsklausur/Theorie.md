# Theorie
## 2 Prinzipien
**a) Wozu dient der Typ Option[+A] in Scala? Wie und wo wird er eingesetzt?**
`Option[+A]` dient dazu, optional vorhandene Werte zu modellieren. Es ist ein Typ mit zwei möglichen Varianten:
- `Some(x)` wenn ein Wert vorhanden ist
- `None` wenn kein Wert vorhanden ist

Einsatzgebiet:
- Als sicherere Alternative zu `null`-Referenzen
- Als Rückgabetyp von Methoden die möglicherweise keinen Wert liefern können (z.B. `find` für Listen)
- Bei der Modellierung von optionalen Parametern oder Eigenschaften

Verwendung:
`val opt: Option[Int] = Some(42)`


**b) Ein Monoid ist eine algebraische Struktur Monoid = (M, $\oplus$, e) mit einer binären Operation $\oplus$ und dem Element e mit folgenden Eigenschaften:**
**- Assoziativität der Operation $\oplus$: (a $\oplus$ b) $\oplus$ c = a $\oplus$ (b $\oplus$ c)**
**- e ist neutrales Element: a $\oplus$ e = a und e $\oplus$ a = a**

**Erläutern Sie, warum diese Eigenschaften für eine *sequentielle* Reduktion einer Liste von Elementen wichtig sind.**
**Erläutern Sie, warum diese Eigenschaften für eine *parallele* Reduktion einer Liste von Elementen wichtig sind.**
\
**Für sequentielle Reduktion:**

- Die Assoziativität (a $\oplus$ b) $\oplus$ c = a $\oplus$ (b $\oplus$ c) garantiert, dass das Ergebnis unabhängig von der Klammerung der Operation ist. Dies ist wichtig, da bei der sequentiellen Reduktion die Elemente nacheinander verarbeitet werden und das Zwischenergebnis jeweils mit dem nächsten Element verknüpft wird.
- Das neutrale Element e wird als Startwert benötigt und muss die Eigenschaft e $\oplus$ a = a haben, damit der erste Wert korrekt verarbeitet wird.

**Für parallele Reduktion:**

- Die Assoziativität ermöglicht es, die Liste in beliebige Teilstücke zu zerlegen und diese parallel zu verarbeiten. Die Teilergebnisse können dann in beliebiger Reihenfolge zusammengeführt werden.
- Das neutrale Element e wird benötigt, um die Teilberechnungen zu initialisieren. Da jeder parallele Zweig mit e startet, muss e $\oplus$ a = a gelten damit die Teilberechnungen korrekt sind.
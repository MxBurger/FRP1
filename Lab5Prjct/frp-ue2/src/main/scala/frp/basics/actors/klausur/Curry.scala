package frp.basics.actors.klausur

def addCurried(x: Int)(y: Int): Int = x + y
// Aufruf:

@main
def main(): Unit = {
  addCurried(2)(3) // Ergebnis: 5

  // Erstellt eine neue Funktion, die 2 zu einer Zahl addiert
  val add2 = addCurried(2)
  // add2 ist jetzt vom Typ: Int => Int

  // Kann mehrfach verwendet werden:
  add2(3) // Ergebnis: 5
  add2(4) // Ergebnis: 6
  add2(5) // Ergebnis: 7
}




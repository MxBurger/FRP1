package frp.basics.actors.klausur

@main
def ThunkMain(): Unit =
  // Dies ist ein Thunk
  val thunk: () => Int = () => {
    println("Berechnung wird ausgeführt")
    42
  }

  // Die Berechnung wird noch nicht ausgeführt
  val lazyValue = thunk
  println("lazyValue wurde noch nicht initialisiert")

  // Erst hier wird die Berechnung tatsächlich durchgeführt
  val result = lazyValue() // Gibt "Berechnung wird ausgeführt" aus und result ist 42
  println(result)
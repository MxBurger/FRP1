import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

def transform(numbers: Seq[Int], f: Int => Int): Future[Seq[Int]] = {
  // Wandelt die Sequenz in eine Future von transformierten Zahlen um
  Future.sequence(
    numbers.map(num => Future(f(num)))
  )
}

def sumSquares(numbers: Seq[Int]): Future[Int] = {
  // Verwendet transform um alle Zahlen zu quadrieren
  val squareFunction = (x: Int) => x * x

  // Transformiert die Zahlen und summiert das Ergebnis
  transform(numbers, squareFunction).map { squares =>
    squares.fold(0)(_ + _) // Summierung mit fold
  }
}

// Beispielverwendung:
val numbers = Seq(1, 2, 3, 4, 5)

@main
def main(): Unit = {

  // Test der transform Funktion
  val doubled = transform(numbers, _ * 2)
  doubled.foreach(println) // Future(Seq(2, 4, 6, 8, 10))

  // Test der sumSquares Funktion
  val sum = sumSquares(numbers)
  sum.foreach(println) // Future(55) (1² + 2² + 3² + 4² + 5² = 55)

  Thread.sleep(1000) // Warten, damit die Futures abgeschlossen werden können
}
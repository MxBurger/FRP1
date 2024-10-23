package fract

import fract.Fract.*

object FractApp {

  def main(args: Array[String]): Unit = {
    println(1~/~2)
    println {
      2~/~3 < 2
    }

    val f1 = (-1~/~2).neg

    println(f1)

    val f2 = Fract(-1, -2)
    val f3 = 1~/~(-2)
    val f4 = -1~/~(-2)
    val f5 = -1~/~2

    val p = 1~/~2 == -1~/~(-2) && 1~/~2 == (-1~/~2).neg
    val n = -1~/~2 == 1~/~(-2)

    val f6 = (1~/~2).neg
    val f7 = (-1~/~2).rec

    (2~/~1).equals(2)

    println(3~/~2 < 2)

    f2 >= f3

    val r = 1~/~2 + 3 + 3~/~4

    val r2 = 1 / (2~/~3) + -2~/~3 * 4~/~5 - 1~/~4 == (60~/~13).rec

  }

}

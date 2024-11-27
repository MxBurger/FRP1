package frp.basics

import scala.runtime.RichInt

object ScalaBasics:

    private def implicitConversion(): Unit =
      // Scala 2
      //val numbers = new RichInt(1) to 3
      //val numbers = intWrapper(1) to 3
      val numbers = 1 to 3
      println(s"numbers: $numbers")

      // Scala 3
      case class MyInt(value: Int):
        def isBig: Boolean = value >= 1000

      given Conversion[Int, MyInt] = i => MyInt(i)

      println(s"1000.isBig: ${1000.isBig}")
      println(s"100.isBig: ${100.isBig}")
    end implicitConversion

    private def extensionMethods(): Unit = {
        extension (n: Int)
          def isEven: Boolean = n % 2 == 0
          def isOdd: Boolean = !n.isEven

        println(s"1000.isEven: ${2.isEven}")
        println(s"1000.isOdd: ${2.isOdd}")

    }

    private def currying(): Unit = {

      val numbers = 1 to 3
      val sumSquares = numbers.foldLeft(0)((s, i) => s + i*i)
      printf("sumSquares: %d\n", sumSquares)

      val foldLeft = numbers.foldLeft("0")( (s, i) => s"($s, $i)")
      println(s"foldLeft: $foldLeft")

      val foldRight = numbers.foldRight("0")( (i, s) => s"($i, $s)")
      println(s"foldRight: $foldRight")

      val f1: ((String, Int) => String) => String = numbers.foldLeft("0")
      val f2: String = f1((s,i) => s"f($s, $i)")

      println(s"f2: $f2")

    }

    private object Orderings:
      trait IntOrdering:
        def less(a: Int, b: Int): Boolean

      object AscendingOrdering extends IntOrdering:
        override def less(a: Int, b: Int): Boolean = a < b

      object DescendingOrdering extends IntOrdering:
        override def less(a: Int, b: Int): Boolean = a > b

    private def implicitParameters(): Unit = {
      import Orderings._

      implicit var defaultOrdering: IntOrdering = AscendingOrdering

      def min(a: Int, b: Int)(implicit ord: IntOrdering) =
        if ord.less(a, b) then a else b

      println(s"min(3, 4)(AscendingOrdering): ${min(3, 4)}")
      println(s"min(3, 4)(DescendingOrdering): ${min(3, 4)(DescendingOrdering)}")

    }

    private def givens(): Unit = {
      import Orderings._

      given IntOrdering with
        override def less(a: Int, b: Int): Boolean = math.abs(a) < math.abs(b)

      def min(a: Int, b: Int)(using ord: IntOrdering) =
        if ord.less(a, b) then a else b

      println(s"min(3, 4)(given): ${min(3, 4)}")
      println(s"min(3, 4)(DescendingOrdering): ${min(3, 4)(using DescendingOrdering)}")
    }

    private def callByName(): Unit = {
      def measure(code: => Unit): Double =
        val start = System.nanoTime()
        code
        (System.nanoTime() - start) / 1_000_000_000.0

      val time: Double = measure {
          (1 to 1_000_000_000).sum
      }

      println(s"time: $time")
    }

    private def companionObject(): Unit = {}

    private def functionTypes(): Unit = {
      val twice = (n: Int) => 2 * n
      val twice2: Function1[Int, Int] = (n: Int) => 2 * n

      println(s"twice(5): ${twice(5)}")

      val sign: PartialFunction[Int, -1 | 0 | 1] = { // union type
        case n if n > 0 => 1
        case n if n < 0 => -1
        case _ => 0
      }

      val signFunc: Int => -1 | 0 | 1 = sign

      println(s"sign(-10): ${sign(-10)}")
      println(s"sign(0): ${sign(0)}")
      println(s"sign(17): ${sign(17)}")
      println(s"sign.isDefinedAt(0)}: ${sign.isDefinedAt(0)}")


    }

    def main(args: Array[String]): Unit =
      println("========= implicitConversion ======")
      implicitConversion()

      println("========= extensionMethods ======")
      extensionMethods()

      println("========= currying ======")
      currying()

      println("========= implicitParameters ======")
      implicitParameters()

      println("========= givens ======")
      givens()

      println("========= callByName ======")
      callByName()

      println("========= companionObject ======")
      companionObject()

      println("========= functionTypes ======")
      functionTypes()
    end main

end ScalaBasics


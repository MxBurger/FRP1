package monads

import java.util.Scanner
import ExprParser.{Success, Failure, parseExpr}
import expr.ExprEvalOption.eval

object ExprEvalMain {

  def main(args: Array[String]): Unit = {
    val bds = Map("x" -> 3.0, "y" -> 4.0, "z" -> 0.0)
    val scn = new Scanner(System.in)
    print("Input expr: ")
    var line = scn.nextLine().trim
    while (!line.startsWith(".")) {
      parseExpr(line) match {
        case Success(expr, _) => {
          println(s"$expr = ${eval(expr, bds)}")
        }
        case Failure(m, r) => {
          println("Failed parse " + m)
        }
      }
      print("Input expr: ")
      line = scn.nextLine().trim
    }
  }

}

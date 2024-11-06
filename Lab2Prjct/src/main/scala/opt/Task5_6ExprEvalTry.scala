package opt

import expr.{Add, Expr, Lit, Min, Mult, Rec, Var}

import java.util.Scanner
import scala.util.{Try, Success, Failure}

// Task 5.6: Expression evaluation with Try

def evalTry(expr: Expr, bds: Map[String, Double]): Try[Double] =
  expr match {
    case Lit(v) => Success(v)
    case Var(n) => bds.get(n) match {
      case Some(v) => Success(v)
      case None => Failure(new Exception(s"Variable $n not found"))
    }
    case Add(l, r) =>
      evalTry(l, bds).flatMap {
        lv => evalTry(r, bds).map(rv => lv + rv)
      }
    case Mult(l, r) =>
      evalTry(l, bds).flatMap {
        lv => evalTry(r, bds).map(rv => lv * rv)
      }
    case Min(e) =>
      evalTry(e, bds).map(v => -v)
    case Rec(e) =>
      evalTry(e, bds).filter(_ != 0).map(v => 1 / v)
  }

object Task5_6ExprEvalTry extends App {

  val bds = Map("x" -> 2.0, "y" -> 3.0, "z" -> 0.0)

  val e1 = Add(Lit(1), Min(Var("x")))
  val tryR1 = evalTry(e1, bds)
  println (s"${e1.toString} = ${tryR1.map(_.toString).getOrElse("undefined")}")

  val e2 = Mult(Lit(1), Min(Var("u")))
  val tryR2 = evalTry(e2, bds)
  println (s"${e2.toString} = ${tryR2.map(_.toString).getOrElse("undefined")}")

  val e3 = Mult(Lit(1), Rec(Var("z")))
  val tryR3 = evalTry(e3, bds)
  println (s"${e3.toString} = ${tryR3.map(_.toString).getOrElse("undefined")}")

}

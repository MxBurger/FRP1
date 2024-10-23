package opt

import expr.{Add, Expr, Lit, Min, Mult, Rec, Var}

import java.util.Scanner
import scala.util.{Try, Success, Failure}

// Task 5.5: Expression evaluation with Option

def evalTry(expr: Expr, bds: Map[String, Double]): Try[Double] = ???

object Task5_5ExprEvalTry extends App {

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

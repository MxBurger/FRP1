package opt

import expr.{Add, BinExpr, Expr, Lit, Min, Mult, Rec, UnyExpr, Var}

import java.util.Scanner

// Task 5.5: Expression evaluation with Option

def evalOption(expr: Expr, bds: Map[String, Double]): Option[Double] =
  expr match {
    case Lit(v) => Some(v)
    case Var(n) => bds.get(n)
    case Add(l, r) =>
      evalOption(l, bds).flatMap {
        lv => evalOption(r, bds).map(rv => lv + rv)
      }
    case Mult(l, r) =>
      evalOption(l, bds).flatMap {
        lv => evalOption(r, bds).map(rv => lv * rv)
      }
    case Min(e) =>
      evalOption(e, bds).map(v => -v)
    case Rec(e) =>
      evalOption(e, bds).filter(_ != 0).map(v => 1 / v)
  }


def infix(expr: Expr): String = {

  def op(e: Expr) =
    e match {
      case Add(_, _) => "+"
      case Mult(_, _) => "*"
      case Min(_) => "-"
      case Rec(_) => "1/"
      case _ => ""
    }

  expr match {
    case Lit(v) => v.toString
    case Var(n) => n
    case b: BinExpr => s"(${infix(b.left)} ${op(b)} ${infix(b.right)})"
    case u: UnyExpr => s"(${op(u)} ${infix(u.sub)})"
  }
}

object Task5_5ExprEvalOption extends App {

  val bds = Map("x" -> 2.0, "y" -> 3.0, "z" -> 0.0)

  val e1 = Add(Lit(1), Min(Var("x")))
  val optR1 = evalOption(e1, bds)
  println (s"${e1.toString} = ${optR1.map(_.toString).getOrElse("undefined")}")

  val e2 = Mult(Lit(1), Min(Var("u")))
  val optR2 = evalOption(e2, bds)
  println (s"${e2.toString} = ${optR2.map(_.toString).getOrElse("undefined")}")

  val e3 = Mult(Lit(1), Rec(Var("z")))
  val optR3 = evalOption(e3, bds)
  println (s"${e3.toString} = ${optR3.map(_.toString).getOrElse("undefined")}")

}

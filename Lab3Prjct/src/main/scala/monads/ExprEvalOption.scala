package monads

import expr.{Rec, Var, *}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}
import util.OptionUtil.*

object ExprEvalOption {

  def eval(expr: Expr, bds: Map[String, Double]) : Option[Double] =
    expr match {
      case Lit(v) => Some(v)
      case Var(n) => option {
        bds(n)
      }

      case Add(l, r) =>
        for {
          lv <- eval(l, bds)
          rv <- eval(r, bds)
        } yield lv + rv

      case Mult(l, r) =>
        for {
          lv <- eval(l, bds)
          rv <- eval(r, bds)
        } yield lv * rv

      case Min(s) =>
        for {
          sr <- eval(s, bds)
        } yield -sr

      case Rec(s) =>
        for {
          sr <- eval(s, bds)
          r <- if (sr == 0.0) then None else Some(1.0 / sr)
        } yield r
    }

  def main(args: Array[String]) : Unit = {

    val bds = Map("x" -> 3.0, "y" -> 4.0, "z" -> 0.0)

    val expr1 = Mult(Var("x"), Rec(Var("y"))) // x * (1 / y)
    val r1 = eval(expr1, bds)
    println(s"$expr1 = $r1")

    val expr2 = Mult(Var("x"), Rec(Var("z"))) // x * (1 / z)
    val r2 = eval(expr2, bds)
    println(s"$expr2 = $r2")

    val expr3 = Mult(Var("x"), Rec(Var("u"))) // x * (1 / u)
    val r3 = eval(expr3, bds)
    println(s"$expr3 = $r3")
  }

}

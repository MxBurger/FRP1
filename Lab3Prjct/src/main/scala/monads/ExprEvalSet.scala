package monads

import expr.*

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object ExprEvalSet {

  def eval(expr: Expr, bds: Map[String, Set[Double]]): Set[Double] =
    expr match {
      case Lit(v) => Set(v)
      case Var(n) => if (bds.contains(n)) bds(n) else Set()
      case Add(l, r) =>
        eval(l, bds).flatMap { lv =>
          eval(r, bds).map { rv =>
            lv + rv
          }
        }
      case Mult(l, r) =>
        eval(l, bds).flatMap { lv =>
          eval(r, bds).map { rv =>
            lv * rv
          }
        }
      case Min(s) =>
        eval(s, bds).map { sr =>
          -sr
        }
      case Rec(s) =>
        eval(s, bds).flatMap { sr =>
          if (sr == 0.0) then Set() else Set(1.0 / sr)
        }
    }

  def main(args: Array[String]): Unit = {

    val bds = Map("x" -> Set(3.0), "y" -> Set(2.0, 3.0))

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

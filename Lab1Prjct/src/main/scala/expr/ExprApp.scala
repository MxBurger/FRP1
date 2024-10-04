package expr

import expr._

object ExprApp {

  def main(args: Array[String]): Unit = {
    testSimplification()
  }

  def testSimplification(): Unit = {
    val expressions = List(
      // rules from exercise sheet
      ("a + 0", Add(Var("a"), Lit(0))),
      ("0 + a", Add(Lit(0), Var("a"))),
      ("a * 0", Mult(Var("a"), Lit(0))),
      ("0 * a", Mult(Lit(0), Var("a"))),
      ("a * 1", Mult(Var("a"), Lit(1))),
      ("1 * a", Mult(Lit(1), Var("a"))),
      ("-(-a)", Min(Min(Var("a")))),
      ("1/(1/a)", Rec(Rec(Var("a")))),
      // more complex expressions
      ("a + b + 0", Add(Add(Var("a"), Var("b")), Lit(0))),
      ("a * (b + 0)", Mult(Var("a"), Add(Var("b"), Lit(0)))),
      ("(a + 0) * (b + 1)", Mult(Add(Var("a"), Lit(0)), Add(Var("b"), Lit(1)))),
      ("-(a * (b + 1))", Min(Mult(Var("a"), Add(Var("b"), Lit(1))))),
      ("1 / (a + b + 1)", Rec(Add(Add(Var("a"), Var("b")), Lit(1))))
    )

    expressions.foreach { case (desc, expr) =>
      println(s"Original: $desc")
      println(s"Infix: ${infix(expr)}")
      println(s"Simplified: ${infix(simplify(expr))}")
      println()
    }
  }
}

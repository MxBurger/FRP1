package expr

import expr._

object ExprApp {

  def main(args: Array[String]): Unit = {
    println("--- Expression evaluation ---")
    testEvaluation()
    println("--- Expression simplification ---")
    testSimplification()
  }

  def testEvaluation(): Unit = {
    val expressions = List(
      (Add(Lit(1), Lit(2)), Map.empty, 3.0), // 1 + 2 = 3
      (Add(Var("a"), Lit(2)), Map("a" -> 1.0), 3.0), // a = 1, a + 2 = 3
      (Mult(Var("a"), Lit(2)), Map("a" -> 3.0), 6.0), // a = 3, a * 2 = 6
      (Min(Var("a")), Map("a" -> 3.0), -3.0), // a = 3, -a = -3
      (Rec(Var("a")), Map("a" -> 3.0), 1.0 / 3.0), // a = 3, 1/a = 1/3
      (Add(Add(Var("a"), Var("b")), Lit(3)), Map("a" -> 1.0, "b" -> 2.0), 6.0), // a = 1, b = 2, a + b + 3 = 6
      (Mult(Add(Var("a"), Var("b")), Lit(2)), Map("a" -> 1.0, "b" -> 2.0), 6.0), // a = 1, b = 2, (a + b) * 2 = 6
      (Min(Add(Var("a"), Var("b"))), Map("a" -> 1.0, "b" -> 2.0), -3.0), // a = 1, b = 2, -(a + b) = -3
      (Rec(Mult(Var("a"), Var("b"))), Map("a" -> 2.0, "b" -> 3.0), 1.0 / 6.0), // a = 2, b = 3, 1/(a * b) = 1/6
      (Add(Mult(Var("a"), Var("b")), Add(Var("c"), Lit(4))), Map("a" -> 1.0, "b" -> 2.0, "c" -> 3.0), 9.0) // a = 1, b = 2, c = 3, a * b + c + 4 = 9
    )

    expressions.foreach { case (expr, bds, expected) =>
      println(s"Expression: ${infix(expr)}")
      if (bds.nonEmpty) {
        println("Bindings:")
        bds.foreach { case (k, v) => println(s"$k = $v") }
      }
      println(s"Evaluated: ${eval(expr, bds)}")
      println(s"Expected: $expected")
      println()
    }
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

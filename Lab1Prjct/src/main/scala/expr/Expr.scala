package expr

sealed trait Expr
// TODO

def infix(expr: Expr): String = ??? // TODO:

def eval(expr: Expr, bds: Map[String, Double]): Double = ???

def simplify(expr: Expr): Expr = ???
  

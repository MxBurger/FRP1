package expr

sealed trait Expr
case class Lit(v: Double) extends Expr
case class Var(n: String) extends Expr
sealed trait BinExpr(val left: Expr, val right: Expr) extends Expr
case class Add(addend: Expr, augent: Expr) extends BinExpr(addend, augent)
case class Mult(multiplicant: Expr, multiplier: Expr) extends BinExpr(multiplicant, multiplier)
sealed trait UnyExpr(val sub: Expr) extends Expr
case class Min(s: Expr) extends UnyExpr(s)
case class Rec(s: Expr) extends UnyExpr(s)

def infix(expr: Expr): String =
  expr match
    case Lit(v) => v.toString
    case Var(name) => name
    case Add(l, r) => s"(${infix(l)} + ${infix(r)})"
    case Mult(l, r) => s"(${infix(l)} * ${infix(r)})"
    case Min(s) => s"(-${infix(s)})"
    case Rec(s) => s"(/ -${infix(s)})"

def eval(expr: Expr, bds: Map[String, Double]): Double =
  expr match {
    case Lit(v) => v
    case Var(n) if bds.contains(n) => bds(n)
    case Var(n) => throw new NoSuchElementException(s"No value for $n")     // be aware not functional
    case Add(l, r) => eval(l, bds) + eval(r, bds)
    case Mult(l, r) => eval(l, bds) * eval(r, bds)
    case Min(s) => - eval(s, bds)
    case Rec(s) => 1 / eval(s, bds)
  }

def simplify(expr: Expr): Expr =
  expr match {
    case l@Lit(_) => l
    case v@Var(_) => v
    case Add(l, r) => {
      val ls = simplify(l)
      val rs = simplify(r)
      (ls, rs) match
        case (Lit(0.0), r) => r
        case (l, Lit(0.0)) => l
        case (l, r) => Add(l, r)
    }
    case Mult(l, r) => {
      val ls = simplify(l)
      val rs = simplify(r)
      (ls, rs) match
        case (Lit(0.0), r) => Lit(0.0)
        case (l, Lit(0.0)) => Lit(0.0)
        case (Lit(1.0), r) => r
        case (l, Lit(1.0)) => l
        case (l, r) => Mult(l, r)
    }
    case Min(s) => {
      val ss = simplify(s)
      ss match
        case Lit(v) => Lit(-v)
        case Min(v) => v
        case (v) => Min(v)
    }
    case Rec(s) => {
      val ss = simplify(s)
      ss match
        case Lit(v) => Lit(1 / v)
        case Rec(v) => v
        case (v) => Rec(v)
    }

  }
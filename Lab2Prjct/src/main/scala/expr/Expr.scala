package expr

sealed trait Expr :
  override def toString: String = infix(this)
case class Lit(v: Double) extends Expr
case class Var(n: String) extends Expr
sealed trait BinExpr(val left: Expr, val right: Expr) extends Expr
case class Add(l: Expr, r: Expr) extends BinExpr(l, r)
case class Mult(l: Expr, r: Expr) extends BinExpr(l, r)
sealed trait UnyExpr(val sub: Expr) extends Expr
case class Min(s: Expr) extends UnyExpr(s)
case class Rec(s: Expr) extends UnyExpr(s)

def infix(expr: Expr): String = {

  def op(e: Expr) =
    e match {
      case Add(_, _)  => "+"
      case Mult(_, _) => "*"
      case Min(_)     => "-"
      case Rec(_)     => "1/"
      case _          => ""
    }

  expr match {
    case Lit(v) => v.toString
    case Var(n) => n
    case b: BinExpr => s"(${infix(b.left)} ${op(b)} ${infix(b.right)})"
    case u: UnyExpr => s"(${op(u)} ${infix(u.sub)})"
  }
}

def eval(expr: Expr, bds: Map[String, Double]): Double =
  expr match {
    case Lit(v) => v
    case Var(n) => bds.apply(n)
    case Add(l, r) => eval(l, bds) + eval(r, bds)
    case Mult(l, r) => eval(l, bds) * eval(r, bds)
    case Min(s) => -eval(s, bds)
    case Rec(s) => 1 / eval(s, bds)
  }


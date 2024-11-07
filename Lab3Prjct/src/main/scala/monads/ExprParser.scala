package monads

import expr.*

import java.util.Scanner
import scala.util.parsing.combinator.JavaTokenParsers

object ExprParser extends JavaTokenParsers {

  def expr: Parser[Expr] = lit | vbl | add | mult | rec | min
  def lit: Parser[Lit] = decimalNumber ^^ { case d => Lit(d.toDouble) }
  def vbl: Parser[Var] = ident ^^ { case n => Var(n) }
  def add: Parser[Add] = "(" ~> expr ~ "+" ~ expr <~ ")" ^^ { case l ~ op ~ r => Add(l, r) }
  def mult: Parser[Mult] = "(" ~> expr ~ "*" ~ expr <~ ")" ^^ { case l ~ op ~ r => Mult(l, r) }
  def min: Parser[Min] = "(" ~> "-" ~> expr <~ ")" ^^ { case s => Min(s) }
  def rec: Parser[Rec] = "(" ~> "/" ~> expr <~ ")" ^^ { case s => Rec(s) }


  def parseExpr(input: String): ParseResult[Expr] = {
    parse(expr, input)
  }

}

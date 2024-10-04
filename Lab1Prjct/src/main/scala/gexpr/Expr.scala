package gexpr

sealed trait Expr[A <: Field[A]]

def eval[A <: Field[A]](expr: Expr[A], bds: Map[String, A]) : A = ???

def simplify[A <: Field[A]](expr: Expr[A]): Expr[A] = ???


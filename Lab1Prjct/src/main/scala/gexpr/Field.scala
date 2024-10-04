package gexpr

trait Field[F] :
  def +(that: F): F
  def *(that: F): F
  def neg: F
  def rec: F
  def isZero : Boolean
  def isOne : Boolean

package fract

import java.util.Objects
import scala.annotation.targetName

final class Fract(_n: Int, _d: Int) extends AnyRef with Ordered[Fract] {
  val (n, d) = normalize(_n, _d)

  def +(that: Fract): Fract = Fract(this.n * that.d + that.n * this.d, this.d * that.d)
  def -(that: Fract): Fract = Fract(this.n * that.d - that.n * this.d, this.d * that.d)
  def *(that: Fract): Fract = Fract(this.n * that.n, this.d * that.d)
  def /(that: Fract): Fract = Fract(this.n * that.d, this.d * that.n)
  def neg: Fract = Fract(-n, d)
  def rec: Fract = Fract(d, n)
  def isZero: Boolean = n == 0.0
  def isOne: Boolean = n == 1.0 && d == 1.0

  def +(n: Int): Fract = Fract(this.n  + n * this.d, this.d)
  def -(n: Int): Fract = Fract(this.n - n * this.d, this.d)
  def *(n: Int): Fract = Fract(this.n * n, this.d)
  def /(n: Int): Fract = Fract(this.n , this.d * n)

  override def compare(that: Fract): Int = {
    val p = this.n * that.d
    val q = that.n * this.d
    if p < q then -1
    else if p > q then 1
                  else 0
  }

  override def hashCode(): Int = Objects.hash(n, d)

  override def equals(obj: Any): Boolean = {
    obj match
      case that : Fract  =>  n == that.n && d == that.d
      case i : Int       =>  n == i && d == 1
      case _             =>  false
  }

  override def toString: String = s"$n~$d"
}

object Fract {
  def apply(n: Int, d: Int) = new Fract(n, d)
  def apply(n: Int) = new Fract(n, 1)

  extension (n : Int) {
    def +(f: Fract): Fract = Fract(n * f.d + f.n, f.d)
    def -(f: Fract): Fract = Fract(f.n - n * f.d, f.d)
    def *(f: Fract): Fract = Fract(f.n * n, f.d)
    def /(f: Fract): Fract = Fract(f.n, f.d * n)

    def ~/~(d: Int) = Fract(n, d)
  }

  implicit def intToFract(n: Int) : Fract = Fract(n, 1)   // Scala 2
  //given intToFract : Conversion[Int, Fract] = i => Fract(i)   // Scala 3

  //given canEqFractInt : CanEqual[Fract, Int] = CanEqual.derived
  //given canEqIntFract : CanEqual[Int, Fract] = CanEqual.derived

}

private def normalize(n: Int, d: Int): (Int, Int) = {
  val gcd = compGcd(n, d)
  if (n < 0 && d < 0) then (-n / gcd, -d / gcd)
  else if (d < 0) then (-n /gcd, -d / gcd)
  else (n / gcd, d / gcd)
}

private def compGcd(x: Int, y: Int): Int =
  if (x < 0 || y < 0) then compGcd(x.abs, y.abs)
  else if (y > x) then compGcd(y, x)
  else if (y == 0) then x
  else compGcd(y, x % y)

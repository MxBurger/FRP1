package fract

import gexpr.Field

import java.util.Objects

// DSL domain specific language

final class Fract(val n: Int,  val d: Int) extends Ordered[Fract] {

  import Fract.{computeGcd}

  def +(that: Fract): Fract = {
    val cd = this.d * that.d
    Fract(this.n * that.d + that.n * this.d, cd)
  }
  def -(that: Fract): Fract = {
    val cd = this.d * that.d
    Fract(this.n * that.d - that.n * this.d, cd)
  }
  def *(that: Fract): Fract =
    Fract(this.n * that.n, this.d * that.d)
  def /(that: Fract): Fract =
    Fract(this.n * that.d, this.d * that.n)
  def +(i: Int): Fract = this + Fract(i, 1)
  def -(i: Int): Fract = this - Fract(i, 1)
  def /(i: Int): Fract = this / Fract(i, 1)
  def *(i: Int): Fract = this * Fract(i, 1)

  def compare(that: Fract): Int =
    Integer.compare(this.n * that.d, that.n * this.d)

  override def toString: String = s"$n/$d"

  override def hashCode: Int = Objects.hash(n, d)

  override def equals(obj: Any): Boolean = {

    // refactor with pattern matching
    if (!obj.isInstanceOf[Fract]) false
    else {
      val that = obj.asInstanceOf[Fract]
      this.n == that.n && this.d == that.d
    }
  }
}

// extension methods can also be defined in a package object
extension (n: Int) {
  def +(f: Fract): Fract = Fract(n, 1) + f
  def -(f: Fract): Fract = Fract(n, 1) - f
  def *(f: Fract): Fract = Fract(n, 1) * f
  def /(f: Fract): Fract = Fract(n, 1) / f

  def ~ (d: Int): Fract = Fract(n, d)
}


object Fract { // companion object for class Fraction

  // this is the old way of doing extension methods
  // implicit def int2Fract(n: Int): Fract = Fract(n, 1)

  def apply(n: Int, d: Int): Fract = {
    val gcd = computeGcd(n, d)
    new Fract(n / gcd, d / gcd)
  }
  def apply(n: Int): Fract = new Fract(n, 1)

  def computeGcd(x: Int, y: Int): Int = { // recursive methods need explicit return type
    if x < 0 || y < 0 then computeGcd(Math.abs(x), Math.abs(y))
    if y > x then computeGcd(y, x)
    else if y == 0 then x
    else computeGcd(y, x % y)
  }
}

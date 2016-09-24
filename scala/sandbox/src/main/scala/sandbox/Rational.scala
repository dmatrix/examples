package main.scala.sandbox

/**
  * Created by jules on 3/13/16.
  * A immutable class to represent and encapsulate rational numbers. This class illustrates how one would go about defining an immutable object,
  * the Scala way. This example is borrowed from Progamming in Scala, 2nd Ed. by Martin Odersky and et al (pg. 111, Listing 6.5
  *
  * Comments adderd below for my clearification and learning knowledge.
  *
  * More than showing how to think of immutable objects class design, it denomstrates how to implement operators by
  * defining them as Scala would—by generating another object not mutating the current state.
  */

/**
  * Class for rational.
  * @param n numerator
  * @param d denominator. Throws an IllegalArgumenException if d == 0
  */
class Rational (n: Int, d: Int) {
  require (d != 0)

  // private variables and initializers
  private val g = gcd (n.abs, d.abs)
  //instance variables
  val numer: Int = n / g
  val denom: Int = d / g

  /**
    * Auxiliary constructor
    * @param n
    * @return Rational
    */
  def this (n: Int) = this (n, 1)

  /**
    * Override the toString method
    * @return
    */
  override def toString = n + "/" + d

  /**
    * Its method operator to "add" two rational numbers. In keeping its immutability, the "+" operator method does not keep state or alter the
    * existing number. Instead it creates a new and returns its value. That idea is consistent with Scala's way of thinking
    * that Scala objects should be immutable and functions or methods only return a new value value without changing itself.
    * @param that Rational number to add to
    * @return a new Rational number
    */
  def + (that: Rational) : Rational =
    new Rational (
      numer * that.denom + that.numer * denom,
      denom * that.denom
  )

  /**
    * Overloading the "+" operator method to add an Integer to this Rational number
    * @param i Integer
    * @return a new Rational number
    */
  def + (i: Int): Rational =
    new Rational (numer + i * denom, denom)

  /**
    * Method operator to "multiply" two rational numbers. In keeping its immutability, the "*" operator method does not keep state or alter the
    * existing number. Instead it creates a new and returns its value. That idea is consistent with Scala's way of thinking
    * that Scala objects should be immutable and functions or methods only return a new value value without changing itself.
    * @param that
    * @return
    */
  def * (that: Rational): Rational =
    new Rational (
      numer * that.numer, denom * that.denom
    )

  /**
    * Overloading the "*" operator method to multiple an Integer with this Rational
    * @param i
    * @return
    */
  def * (i: Int): Rational =
    new Rational (numer * i, denom)

  /**
    * Method operator to "-" two Rational numbers
    * @param that
    * @return new Rational number
    */
  def - (that: Rational): Rational =
    new Rational(
      numer * that.denom - that.numer * denom,
      denom * that.denom
    )

  /**
    * Overloading the '-' operator method to subtract an Integer from this Rational number
    * @param i
    * @return new Rational number
    */
  def - (i: Int): Rational =
    new Rational(numer - i * denom, denom)

  /**
    * Operator method for "/" (division)
    * @param that
    * @return new Rational Number
    */
  def / (that: Rational): Rational =
    new Rational (numer * that.denom, denom * that.numer)

  /**
    * Overloading the "/" to divide this Rational Number by an Integer
    * @param i
    * @return new Rational Number
    */
  def / (i: Int): Rational =
    new Rational (numer, denom * i)
  /**
    * Return boolean if the Rational argument is less than the current Rational (the one on which the method is invoked)
    * @param that
    * @return boolean if that Rational number is less than the one on which this method was invoked
    */
  def lessThan (that: Rational): Boolean = this.numer * that.denom < that.numer * this.denom

  /**
    * Return the max of two rational numbers: that (parameter) and this (current number)
    * @param that
    * @return Rataionl
    */
  def max (that: Rational): Rational = if (this.lessThan(that)) that else this

  /**
    * Method to commute the greates common divisor so that we can normalize our Rational Numbers
    * @param a
    * @param b
    * @return Int as the greatest common divisor of a and b
    */
  private def gcd (a: Int, b: Int): Int = if (b == 0) a else  gcd (b, a % b)
}

object Rational {
  /**
    * Driver to test some rational numbers
    * @param args
    */
  def main(args: Array[String]) {

    val r1 = new Rational (1, 2)
    val r2 = new Rational (2, 3)

    val r3 = r1 + r2
    println( "r1 + r2 = " + r3)

    val r4 = r1 + (r1 * r2)
    println( "r1 + (r1 * r2) = " + r4)

  }
}

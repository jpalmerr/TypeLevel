/*
If a type A can form a Semigroup it has an associative binary operation.

trait Semigroup[A] {
  def combine(x: A, y: A): A
}
 */

import cats.Semigroup

implicit val intAdditionSemigroup: Semigroup[Int] = new Semigroup[Int] {
  def combine(x: Int, y: Int): Int = x + y
}

//implicit val intAdditionSemigroup: Semigroup[Int] = (x: Int, y: Int) => x + y

val x = 1
val y = 2
val z = 3

Semigroup[Int].combine(x, y)
// res0: Int = 3

Semigroup[Int].combine(x, Semigroup[Int].combine(y, z))
// res1: Int = 6

Semigroup[Int].combine(Semigroup[Int].combine(x, y), z)
// res2: Int = 6

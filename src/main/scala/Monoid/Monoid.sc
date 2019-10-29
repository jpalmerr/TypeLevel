import cats.data.NonEmptyList
import cats.instances.all._
import cats.kernel.Semigroup

// Monoid extends the power of Semigroup by providing an additional empty value.

trait Semigroup[A] {
  def combine(x: A, y: A): A
}

trait Monoid[A] extends Semigroup[A] {
  def empty: A
}

// notice extends

/* This empty value should be an identity for the combine operation, which means:

  combine(x, empty) = combine(empty, x) = x
 */

import cats.Monoid

implicit val intAdditionMonoid: Monoid[Int] = new Monoid[Int] {
  def empty: Int = 0
  def combine(x: Int, y: Int): Int = x + y
}

val x = 1

Monoid[Int].combine(x, Monoid[Int].empty)
// Int = 1
Monoid[Int].combine(Monoid[Int].empty, x)
// Int = 1

// example usage: collapsing lists


def combineAll[A: Monoid](as: List[A]): A =
  as.foldLeft(Monoid[A].empty)(Monoid[A].combine)

import cats.implicits._

combineAll(List(1, 2, 3))

combineAll(List("hello", " ", "world"))

combineAll(List(Map('a' -> 1), Map('a' -> 2, 'b' -> 3), Map('b' -> 4, 'c' -> 5)))

combineAll(List(Set(1, 2), Set(2, 3, 4, 5)))

// This function is provided in Cats as Monoid.combineAll.

Monoid.combineAll(List(1, 2, 3))
// Int = 6
Monoid.combineAll(List("hello", " ", "world"))
// String = hello world

/*
There are some types that can form a Semigroup but not a Monoid.
For example, the following NonEmptyList type forms a semigroup through ++,
but has no corresponding identity element to form a monoid.
 */

//How then can we collapse a List[NonEmptyList[A]] ?
// For such types that only have a Semigroup we can lift into Option to get a Monoid

// lifting and combining of Semigroups into Option is provided by Cats as
// Semigroup.combineAllOption.

val list = List(NonEmptyList(1, List(2, 3)), NonEmptyList(4, List(5, 6)))
val lifted = list.map(nel => Option(nel))

Monoid.combineAll(lifted)
// Some(NonEmptyList(1, 2, 3, 4, 5, 6))

Semigroup.combineAllOption(list)
// Some(NonEmptyList(1, 2, 3, 4, 5, 6))


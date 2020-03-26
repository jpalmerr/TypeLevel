// the following concatenate

def sumInts(list: List[Int]) : Int = list.foldRight(0)(_ + _)

def concatStrings(list: List[String]): String = list.foldRight("")(_ ++ _)

def unionSets[A](list: List[Set[A]]): Set[A] = list.foldRight(Set.empty[A])(_ union _)

val ints = List(1, 2, 3)
sumInts(ints) // Int = 6

val strings = List("a", "b", "c")
concatStrings(strings) // String = abc

/*
All of these follow the same pattern:
- initial value (0, empty string, empty set)
- combining function (+, ++, union).

We’d like to abstract over this so we can write the function once instead of once for every type
so we pull out the necessary pieces into an interface
 */

trait Monoid[A] {
  def empty: A
  def combine(x: A, y: A): A
}

// Implementation for Int
val intAdditionMonoid: Monoid[Int] = new Monoid[Int] {
  def empty: Int = 0
  def combine(x: Int, y: Int): Int = x + y
}

val concatStringMonoid: Monoid[String] = new Monoid[String] {
  override def empty: String = ""

  override def combine(x: String, y: String) : String = x ++ y
}

println(intAdditionMonoid.combine(1, 2)) // 3

// We can now write the functions above against this interface

def combineAll[A](list: List[A], A: Monoid[A]): A = list.foldRight(A.empty)(A.combine)

combineAll(ints, intAdditionMonoid) // 6

combineAll(strings, concatStringMonoid) // abc


/* implicit derivation:

Note that a Monoid[Pair[A, B]] is derivable given Monoid[A] and Monoid[B]:
 */

final case class Pair[A, B](first: A, second: B)

def deriveMonoidPair[A, B](A: Monoid[A], B: Monoid[B]): Monoid[Pair[A, B]] =
  new Monoid[Pair[A, B]] {
    def empty: Pair[A, B] = Pair(A.empty, B.empty)

    def combine(x: Pair[A, B], y: Pair[A, B]): Pair[A, B] =
      Pair(A.combine(x.first, y.first), B.combine(x.second, y.second))
  }

// LAWS

/*
Conceptually, all type classes come with laws.

For instance, the Monoid type class requires that:
combine be associative
empty be an identity element for combine

=>

combine(x, combine(y, z)) = combine(combine(x, y), z)
combine(x, id) = combine(id, x) = x
 */


val list = List(1, 2, 3, 4, 5)
val (left, right) = list.splitAt(2)

// Imagine the following two operations run in parallel
val sumLeft = combineAll(left, intAdditionMonoid)
// sumLeft: Int = 3

val sumRight = combineAll(right, intAdditionMonoid)
// sumRight: Int = 12

// Now gather the results
val result = intAdditionMonoid.combine(sumLeft, sumRight)
// result: Int = 15

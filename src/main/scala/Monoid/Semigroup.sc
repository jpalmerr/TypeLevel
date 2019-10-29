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
//Int = 3

Semigroup[Int].combine(x, Semigroup[Int].combine(y, z))
//Int = 6

Semigroup[Int].combine(Semigroup[Int].combine(x, y), z)
//Int = 6

// Infix syntax is also available for types that have a Semigroup instance.

import cats.implicits._

1 |+| 2
// Int = 3


//A more compelling example which we’ll see later in this tutorial is the Semigroup for Maps.
// (This is cool!!!)

val map1 = Map("hello" -> 1, "world" -> 1)
val map2 = Map("hello" -> 2, "cats"  -> 3)

Semigroup[Map[String, Int]].combine(map1, map2)
// Map[String,Int] = Map(hello -> 3, cats -> 3, world -> 1)

map1 |+| map2
// scala.collection.immutable.Map[String,Int] = Map(hello -> 3, cats -> 3, world -> 1)


/* Some example instances
  cats gives us instances out the box
 */

Semigroup[Int]
Semigroup[String]
Semigroup[List[Byte]]

// even on our own types
trait Foo
Semigroup[List[Foo]] // !!
// cats.kernel.Semigroup[List[Foo]]

// MERGING MAPS

/* Consider a function that merges two Maps that combines values if they share the same key.

 It is straightforward to write these for Maps with values of type say, Int or List[String],
 but we can write it once and for all for any type with a Semigroup instance.
 */

def optionCombine[A: Semigroup](a: A, opt: Option[A]): A =
  opt.map(a |+| _).getOrElse(a)

def mergeMap[K, V: Semigroup](lhs: Map[K, V], rhs: Map[K, V]): Map[K, V] =
  lhs.foldLeft(rhs) {
    case (acc, (k, v)) => acc.updated(k, optionCombine(v, acc.get(k)))
  }

val xm1 = Map('a' -> 1, 'b' -> 2)
val xm2 = Map('a' -> 3, 'c' -> 4)

mergeMap(xm1, xm2)
// Map(a -> 4, c -> 4, b -> 2)

val ym1 = Map(1 -> List("hello"))
val ym2 = Map(2 -> List("cats"), 1 -> List("world"))

// and now on a different type
val ym = mergeMap(ym1, ym2)
// Map(2 -> List(cats), 1 -> List(hello, world))


// EXPLOITING LAWS

/* Since we know Semigroup#combine must be associative,
we can exploit this when writing code against Semigroup

For instance, to sum a List[Int]
we can choose to either foldLeft or foldRight
since all that changes is associativity
 */

val leftwards = List(1, 2, 3).foldLeft(0)(_ |+| _)
// Int = 6

val rightwards = List(1, 2, 3).foldRight(0)(_ |+| _)
// Int = 6

// Associativity also allows us to split a list apart and sum the parts in parallel,
// gathering the results in the end.

val list = List(1, 2, 3, 4, 5)

val (left, right) = list.splitAt(2)
val sumLeft = left.sum
val sumRight = right.sum
// =>
val result = sumLeft |+| sumRight
// Int = 15


/*
However, given just Semigroup we cannot write the above expressions generically.
For instance, we quickly run into issues if we try to write a generic combineAll function.

THIS MAKES SENSE:

- we don't have an identity... what would go in the fold?

 */

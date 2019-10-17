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





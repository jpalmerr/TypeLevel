/*
Comonad is a Functor and provides duals of the Monad pure and flatMap functions.
A dual to a function has the same types but the direction of the arrows are reversed.
 */

/*
Monads have pure from Applicative
- gives you the ability to wrap a value A using the type constructor giving an F[A].

Comonad has extract
- takes an F[A] and extracts the A
 */

import cats._
import cats.data._
import cats.implicits._
import cats.syntax.comonad._
import cats.instances.list._

NonEmptyList.of(1,2,3).extract // Int = 1

/*
we cannot write extract for list because lists can be empty,
but we can implement the coflatMap
 */

List(1,2,3,4,5).coflatMap(identity)

//List[List[Int]] = List(List(1, 2, 3, 4, 5), List(2, 3, 4, 5), List(3, 4, 5), List(4, 5), List(5))


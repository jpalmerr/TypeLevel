/*
A FunctionK transforms values from one first-order-kinded type
  - (a type that takes a single type parameter, such as List or Option)
into another first-order-kinded type.

This transformation is universal:

FunctionK[List, Option] will translate all List[A] values into an Option[A] value for all possible types of A.
 */

// ORDINARY FUNCTIONS

def firstMethod(l: List[Int]): Option[Int] = l.headOption

// we could have written this as a function value

val firstVal: List[Int] => Option[Int] = l => l.headOption

// -> just syntatic sugar for Function1 which is a really simple type

trait MyFunction1[A, B] {
  def apply(a: A): B
}

// using generics
def firstGeneric[A](l: List[A]): Option[A] = l.headOption

// however the apply doesn't take a generic type parameter so not quite right

// HIGHER KINDS

trait MyFunctionK[F[_], G[_]] {
  def apply[A](fa: F[A]): G[A]
}

// now apply has generic type parameter

/*
Cats provides this as functionK
 */

import cats.arrow.FunctionK

val first: FunctionK[List, Option] = new FunctionK[List, Option] {
  def apply[A](l: List[A]): Option[A] = l.headOption
}

/*

when plug in working :D
```
import cats.~>

val first: List ~> Option = λ[List ~> Option](_.headOption)
```
 */


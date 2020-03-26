/*
Functor is a type class that abstracts over type constructors that can be map‘ed over

A Functor instance must obey two laws:

Composition:
   - Mapping with f and then again with g is the same as mapping once with the composition of f and g
          -fa.map(f).map(g) = fa.map(f.andThen(g))

Identity:
    - Mapping with the identity function is a no-op
          - fa.map(x => x) = fa


Useful for when you're trying to map nested data types...
*/

import cats.Functor
import cats.implicits._

val listOption: List[Option[Int]] = List(Some(1), None, Some(2))

Functor[List].compose[Option].map(listOption)(_ + 1)
// List[Option[Int]] = List(Some(2), None, Some(3))

import cats.data.Nested
import cats.implicits._

val nested: Nested[List, Option, Int] = Nested(listOption)
// Nested(List(Some(1), None, Some(2)))

nested.map(_ + 1).value
// List[Option[Int]] = List(Some(2), None, Some(3))

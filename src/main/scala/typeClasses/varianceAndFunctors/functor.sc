/*
Functor is a type class that abstracts over type constructors that can be map‘ed over.
Examples of such type constructors are List, Option, and Future
 */

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

// Example implementation for Option
implicit val functorForOption: Functor[Option] = new Functor[Option] {
  def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa match {
    case None    => None
    case Some(a) => Some(f(a))
  }
}

/*
A Functor instance must obey two laws:

Composition: Mapping with f and then again with g is the same as mapping once with the composition of f and g
fa.map(f).map(g) = fa.map(f.andThen(g))

Identity: Mapping with the identity function is a no-op
fa.map(x => x) = fa
 */

/*
Another way of viewing a Functor[F] is that F allows the lifting of a pure function
A => B into the effectful function F[A] => F[B].

We can see this if we re-order the map signature above
 */

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]

  def lift[A, B](f: A => B): F[A] => F[B] =
    fa => map(fa)(f)
}

/*
If you’ve ever found yourself working with nested data types such as
Option[List[A]] or List[Either[String, Future[A]]] and tried to map over it,

you’ve most likely found yourself doing something like _.map(_.map(_.map(f))).

As it turns out, Functors compose, which means if F and G have Functor instances, then so does F[G[_]].
 */

import cats.Functor
import cats.implicits._

val listOption = List(Some(1), None, Some(2))
Functor[List].compose[Option].map(listOption)(_ + 2) // List(Some(3), None, Some(4))

import cats.data.Nested

val nested: Nested[List, Option, Int] = Nested(listOption)
nested.map(_ + 1).value // List(Some(2), None, Some(3))


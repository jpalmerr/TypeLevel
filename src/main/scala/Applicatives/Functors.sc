/* Functor is a type class that abstracts over type constructors that can be map‘ed over.
Examples of such type constructors are List, Option, and Future.
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

- fa.map(f).map(g) = fa.map(f.andThen(g))

Identity: Mapping with the identity function is a no-op

- fa.map(x => x) = fa
 */

// A different view:

/* Another way of viewing a Functor[F] is that F allows the lifting of a pure function
A => B into the effectful function F[A] => F[B].
We can see this if we re-order the map signature above.
 */

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]

  def lift[A, B](f: A => B): F[A] => F[B] =
    fa => map(fa)(f)
}

// Effect Management

/*
The F in Functor is often referred to as an “effect” or “computational context.”

 */

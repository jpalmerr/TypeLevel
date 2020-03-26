import cats.Applicative

trait Traverse[F[_]] {
  def traverse[G[_]: Applicative, A, B](fa: F[A])(f: A => G[B]): G[F[B]]
}

// sequencing

/*
Sometimes you will be given a traversable that has effectful values already, such as a List[Option[A]].
Since the values themselves are effects, traversing with identity will turn the traversable “inside out.”
*/

import cats.implicits._

val list = List(Some(1), Some(2), None)
val traversed = list.traverse(identity)
// Option[List[Int]] = None

// Cats provides a convenience method for this called sequence

val sequenced = list.sequence
// Option[List[Int]] = None

/* Traversables are Functors

As it turns out every Traverse is a lawful Functor.
By carefully picking the G to use in traverse we can implement map.
 */

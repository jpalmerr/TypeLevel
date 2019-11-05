Option(Option(1)).flatten
// Option[Int] = Some(1)
Option(None).flatten
// Option[Nothing] = None
List(List(1),List(2,3)).flatten
// List[Int] = List(1, 2, 3)


/* Monad instances

If Applicative is already present and flatten is well-behaved,
extending the Applicative to a Monad is trivial.

flatMap is just map followed by flatten.
Conversely, flatten is just flatMap using the identity function x => x
 */

// FlatMap - a weakened Monad

// A closely related type class is FlatMap which is identical to Monad, minus the pure method.
// The laws for FlatMap are just the laws of Monad that don’t mention pure.

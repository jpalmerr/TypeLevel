/*
Chain is a data structure that allows constant time prepending and appending.
This makes it especially efficient when used as a Monoid, e.g. with Validated or Writer.
As such it aims to be used where List and Vector incur a performance penalty
 */

// simple adt

sealed abstract class Chain[+A]

final case object Empty extends Chain[Nothing]
final case class Singleton[A](a: A) extends Chain[A]
final case class Append[A](left: Chain[A], right: Chain[A]) extends Chain[A]
final case class Wrap[A](seq: Seq[A]) extends Chain[A]

/*
NonEmptyChain is the non empty version of Chain it does not have a Monoid instance
since it cannot be empty, but it does have a Semigroup instance
 */

import cats.data._

NonEmptyChain(1, 2, 3, 4)

NonEmptyChain.fromNonEmptyList(NonEmptyList(1, List(2, 3)))
NonEmptyChain.fromNonEmptyVector(NonEmptyVector(1, Vector(2, 3)))
NonEmptyChain.one(1)


NonEmptyChain.fromChain(Chain(1, 2, 3))
// Option[cats.data.NonEmptyChain[Int]] = Some(Chain(1, 2, 3))

NonEmptyChain.fromSeq(List.empty[Int])
// Option[cats.data.NonEmptyChain[Int]] = None

NonEmptyChain.fromSeq(Vector(1, 2, 3))
// Option[cats.data.NonEmptyChain[Int]] = Some(Chain(1, 2, 3))

NonEmptyChain.fromChainAppend(Chain(1, 2, 3), 4)
// res7: cats.data.NonEmptyChain[Int] = Chain(1, 2, 3, 4)

NonEmptyChain.fromChainAppend(Chain.empty[Int], 1)
// res8: cats.data.NonEmptyChain[Int] = Chain(1)

NonEmptyChain.fromChainPrepend(1, Chain(2, 3))
// res9: cats.data.NonEmptyChain[Int] = Chain(1, 2, 3)


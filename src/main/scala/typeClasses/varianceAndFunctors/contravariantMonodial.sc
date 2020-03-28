/*
The ContravariantMonoidal type class is for Contravariant functors
that can define a product function and a unit function.
 */

import cats.Contravariant

trait ContravariantMonoidal[F[_]] extends Contravariant[F] {
  def unit: F[Unit]

  def product[A, B](fa: F[A], fc: F[B]): F[(A, B)]

  def contramap2[A, B, C](fb: F[B], fc: F[C])(f: A => (B, C)): F[A] =
    contramap(product(fb, fc))(f)
}

/*
Basically, if you have two contexts F[B] and F[C] for types B and C
as well as a way to produce types B and C simultaneously from a type A,
then ContravariantMonoidal allows you to obtain a context F[A] for the type A.
 */

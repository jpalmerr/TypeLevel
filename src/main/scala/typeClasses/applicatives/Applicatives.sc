// Applicative extends Functor with an ap and pure method

import cats.{Applicative, Functor}
import cats.implicits._
import cats.instances._


trait ApplicativeExample[F[_]] extends Functor[F] {
  def ap[A, B](ff: F[A => B])(fa: F[A]): F[B]

  def pure[A](a: A): F[A]

  def map[A, B](fa: F[A])(f: A => B): F[B] = ap(pure(f))(fa)
}

/* pure wraps the value into the type constructor
 - for Option this could be Some(_),
 for Future Future.successful,
 and for List a singleton list.
 */

/*
Such an Applicative must obey three laws:

Associativity: No matter the order in which you product together three values, the result is isomorphic

fa.product(fb).product(fc) ~ fa.product(fb.product(fc))

With map, this can be made into an equality with

fa.product(fb).product(fc) = fa.product(fb.product(fc)).map { case (a, (b, c)) => ((a, b), c) }

Left identity: Zipping a value on the left with unit results in something isomorphic to the original value

pure(()).product(fa) ~ fa

As an equality: pure(()).product(fa).map(_._2) = fa

Right identity: Zipping a value on the right with unit results in something isomorphic to the original value

fa.product(pure(())) ~ fa

As an equality: fa.product(pure(())).map(_._1) = fa
 */

/* Effect Management
If we view Functor as the ability to work with a single effect,
Applicative encodes working with multiple independent effects.
 */

/* Applicatives compose
Like Functor, Applicatives compose. If F and G have Applicative instances, then so does F[G[_]].
 */

/* Traverse
The straightforward way to use product and map (or just ap) is to compose n independent effects,
where n is a fixed number.
In fact there are convenience methods named apN, mapN, and tupleN
(replacing N with a number 2 - 22) to make it even easier.
 */

import java.sql.Connection

val username: Option[String] = Some("username")
val password: Option[String] = Some("password")
val url: Option[String] = Some("some.login.url.here")

// Stub for demonstration purposes
def attemptConnect(username: String, password: String, url: String): Option[Connection] = None


// We know statically we have 3 Options, so we can use map3 specifically.
Applicative[Option].map3(username, password, url)(attemptConnect)

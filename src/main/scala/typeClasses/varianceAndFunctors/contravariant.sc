/* The Contravariant type class is for functors that define a contramap function with the following type:

def contramap[A, B](fa: F[A])(f: B => A): F[B]
 */

/*
Generally speaking, if you have some context F[A] for type A, and you can get an A value out of a B value
— Contravariant allows you to get the F[B] context for B.
 */

import cats._

import cats.implicits._

final case class Money(amount: Int)
final case class Salary(size: Money)

implicit val showMoney: Show[Money] = Show.show(m => s"$$${m.amount}")

/*
If we want to show a Salary instance, we can just convert it to a Money instance and show that instead.

Let’s use Show’s Contravariant:
 */

implicit val ShowSalary: Show[Salary] = showMoney.contramap( _.size)

Salary(Money(1000)).show // String = $1000

// contravariance for Ordering

import scala.math.Ordered._ // >

implicit val moneyOrdering: Ordering[Money] = Ordering.by(_.amount)

Money(100) < Money(200) // true






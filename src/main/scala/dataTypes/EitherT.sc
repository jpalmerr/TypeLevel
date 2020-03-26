/*
EitherT[F[_], A, B] is a lightweight wrapper for F[Either[A, B]]

for example when you find yourself with a return type of an IO of an Either
a common use case at work
 */

import cats.data.EitherT
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

def parseDouble(s: String): Either[String, Double] =
  Try(s.toDouble).map(Right(_)).getOrElse(Left(s"$s is not a number"))
// parseDouble: (s: String)Either[String,Double]

def divide(a: Double, b: Double): Either[String, Double] =
  Either.cond(b != 0, a / b, "Cannot divide by zero")
// divide: (a: Double, b: Double)Either[String,Double]


def parseDoubleAsync(s: String): Future[Either[String, Double]] =
  Future.successful(parseDouble(s))
def divideAsync(a: Double, b: Double): Future[Either[String, Double]] =
  Future.successful(divide(a, b))

def divisionProgramAsync(inputA: String, inputB: String): EitherT[Future, String, Double] =
  for {
    a <- EitherT(parseDoubleAsync(inputA))
    b <- EitherT(parseDoubleAsync(inputB))
    result <- EitherT(divideAsync(a, b))
  } yield result

divisionProgramAsync("4", "2").value
// scala.concurrent.Future[Either[String,Double]] = Future(<not completed>)

divisionProgramAsync("a", "b").value
// scala.concurrent.Future[Either[String,Double]] = Future(<not completed>)

/*
Note that when F is a monad, then EitherT will also form a monad,
allowing monadic combinators such as flatMap to be used in composing EitherT values.
 */

// From A or B to EitherT[F, A, B]

val number: EitherT[Option, String, Int] = EitherT.rightT(5) // EitherT(Some(Right(5)))
val alsoNumber: EitherT[Option, String, Int] = EitherT.pure(5) // EitherT(Some(Right(5)))
val error: EitherT[Option, String, Int] = EitherT.leftT("Not a number") // EitherT(Some(Left(Not a number)))

// From F[A] or F[B] to EitherT[F, A, B]

/*
Similarly, use EitherT.left and EitherT.right to convert an F[A] or an F[B] into an EitherT.
It is also possible to use EitherT.liftF as an alias for EitherT.right.
 */

val numberO: Option[Int] = Some(5)
val errorO: Option[String] = Some("Not a number")

val aNumber: EitherT[Option, String, Int] = EitherT.right(numberO)
val alsoANUmber: EitherT[Option, String, Int] = EitherT.liftF(numberO)
val error: EitherT[Option, String, Int] = EitherT.left(errorO)

// From Either[A, B] or F[Either[A, B]] to EitherT[F, A, B]

/*
Use EitherT.fromEither to lift a value of Either[A, B] into EitherT[F, A, B].
An F[Either[A, B]] can be converted into EitherT using the EitherT constructor.
 */

val numberE: Either[String, Int] = Right(100)
val errorE: Either[String, Int] = Left("Not a number")
val numberFE: List[Either[String, Int]] = List(Right(250))

val numberET: EitherT[List, String, Int] = EitherT.fromEither(numberE) // EitherT(List(Right(100)))
val errorET: EitherT[List, String, Int] = EitherT.fromEither(errorE) // EitherT(List(Left(Not a number)))
val numberFET: EitherT[List, String, Int] = EitherT(numberFE) // EitherT(List(Right(250)))

// From Option[B] or F[Option[B]] to EitherT[F, A, B]

/*
An Option[B] or an F[Option[B]], along with a default value,
can be passed to EitherT.fromOption and EitherT.fromOptionF,
respectively, to produce an EitherT.
 */

val myOption: Option[Int] = None
// myOption: Option[Int] = None

val errorT: EitherT[Future, String, Int] = EitherT.leftT("foo")
// errorT: cats.data.EitherT[scala.concurrent.Future,String,Int] = EitherT(Future(Success(Left(foo))))

val error: Future[Either[String, Int]] = errorT.value
// error: scala.concurrent.Future[Either[String,Int]] = Future(Success(Left(foo)))

val myOptionList: List[Option[Int]] = List(None, Some(2), Some(3), None, Some(5))
// myOptionList: List[Option[Int]] = List(None, Some(2), Some(3), None, Some(5))

val myOptionET = EitherT.fromOption[Future](myOption, "option not defined")
// myOptionET: cats.data.EitherT[scala.concurrent.Future,String,Int] = EitherT(Future(Success(Left(option not defined))))

val myOptionListET = EitherT.fromOptionF(myOptionList, "option not defined")
// myOptionListET: cats.data.EitherT[List,String,Int] = EitherT(List(Left(option not defined), Right(2), Right(3), Left(option not defined), Right(5)))

// Extracting an F[Either[A, B]] from an EitherT[F, A, B]


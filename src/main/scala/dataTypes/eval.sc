// Eval is a data type for controlling synchronous evaluation.

import cats.Eval
// import cats.Eval

import cats.implicits._
// import cats.implicits._

val eager = Eval.now {
  println("Running expensive calculation...")
  1 + 2 * 3
}
// eager: cats.Eval[Int] = Now(7)

eager.value // 7

val lazyEval = Eval.later {
  println("Running expensive calculation...")
  1 + 2 * 3
}
// lazyEval: cats.Eval[Int] = cats.Later@6c2b03e9

lazyEval.value
// Running expensive calculation...
// Int = 7

lazyEval.value
// Int = 7
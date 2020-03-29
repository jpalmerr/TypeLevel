```scala
final case class Const[A, B](getConst: A)
```

It would seem Const gives us no benefit over a data type that would simply not have the second type parameter.
However, while we don’t directly use the second type parameter, its existence becomes useful in certain contexts.

```
trait Traverse[F[_]] extends Foldable[F] {
  def traverse[G[_] : Applicative, A, X](fa: F[A])(f: A => G[X]): G[F[X]]

  def foldMap[A, B : Monoid](fa: F[A])(f: A => B): B = {
    val const: Const[B, F[Nothing]] = traverse[Const[B, ?], A, Nothing](fa)(a => Const(f(a)))
    const.getConst
  }
}
```

The concept of having a type:

Const[B, Z] (for any Z) is the moral equivalent of just B

is very useful

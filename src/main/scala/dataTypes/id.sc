// type Id[A] = A

import cats._

val x: Id[Int] = 1
// cats.Id[Int] = 1

val y: Int = x
// Int = 1

import cats.Functor

val one: Int = 1
// one: Int = 1

Functor[Id].map(one)(_ + 1) // 2

/*
def map[A, B](fa: Id[A])(f: A => B): Id[B]
def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B]
def coflatMap[A, B](a: Id[A])(f: Id[A] => B): Id[B]
 */

import cats.Monad

Monad[Id].map(one)(_ + 1) // 2

Monad[Id].flatMap(one)(_ + 1)  // 2

import cats.Comonad

Comonad[Id].coflatMap(one)(_ + 1)
// cats.Id[Int] = 2
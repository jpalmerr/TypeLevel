/*
A pattern that appears sometimes in functional programming is that of a
function first computing some kind of intermediate result
and then passing that result to another function which was passed in as an argument,
in order to delegate the computation of the final result.
 */

case class User(id: Int, name: String, age: Int)
sealed trait UserUpdateResult
case class Succeeded(updatedUserId: Int) extends UserUpdateResult
case object Failed extends UserUpdateResult

import cats.Eval
// import cats.Eval

def updateUser(persistToDatabase: User => Eval[UserUpdateResult])
              (existingUser: User, newName: String, newAge: Int): Eval[UserUpdateResult] = {
  val trimmedName = newName.trim
  val cappedAge = newAge max 150
  val updatedUser = existingUser.copy(name = trimmedName, age = cappedAge)

  persistToDatabase(updatedUser)
}

// This pattern is known as “continuation passing style”

import cats.data.ContT
// import cats.data.ContT

def updateUserCont(existingUser: User,
                   newName: String,
                   newAge: Int): ContT[Eval, UserUpdateResult, User] =
  ContT.apply[Eval, UserUpdateResult, User] { next =>
    val trimmedName = newName.trim
    val cappedAge = newAge min 150
    val updatedUser = existingUser.copy(name = trimmedName, age = cappedAge)

    next(updatedUser)
  }

/* ContT[M, A, B]:

  M => higher kinded type
  A => Return type inside higher type
  B => Input type
 */

val existingUser = User(100, "AliceX", 42)
val computation = updateUserCont(existingUser, "Alice", 41)

val eval = computation.run { user =>
  Eval.later {
    println(s"Persisting updated user to the DB: $user")
    Succeeded(user.id)
  }
}

// execute
eval.value

/* The point is that ContT is a monad,
so by rewriting our function into a ContT we gain composibility for free.
 */

val anotherComputation = computation.map { user =>
  Map(
    "id" -> user.id.toString,
    "name" -> user.name,
    "age" -> user.age.toString
  )
}

val anotherEval = anotherComputation.run { userFields =>
  Eval.later {
    println(s"Persisting these fields to the DB: $userFields")
    Succeeded(userFields("id").toInt)
  }
}

anotherEval.value

// And we can use flatMap to chain multiple ContTs together.

val updateUserModel: ContT[Eval, UserUpdateResult, User] =
  updateUserCont(existingUser, "Bob", 200).map { updatedUser =>
    println("Updated user model")
    updatedUser
  }

val persistToDb: User => ContT[Eval, UserUpdateResult, UserUpdateResult] = {
  user =>
    ContT.apply[Eval, UserUpdateResult, UserUpdateResult] { next =>
      println(s"Persisting updated user to the DB: $user")

      next(Succeeded(user.id))
    }
}

val publishEvent: UserUpdateResult => ContT[Eval, UserUpdateResult, UserUpdateResult] = {
  userUpdateResult =>
    ContT.apply[Eval, UserUpdateResult, UserUpdateResult] { next =>
      userUpdateResult match {
        case Succeeded(userId) =>
          println(s"Publishing 'user updated' event for user ID $userId")
        case Failed =>
          println("Not publishing 'user updated' event because update failed")
      }

      next(userUpdateResult)
    }
}

val chainOfContinuations =
  updateUserModel flatMap persistToDb flatMap publishEvent
// chainOfContinuations: cats.data.ContT[cats.Eval,UserUpdateResult,UserUpdateResult] = FromFn(AndThen$946122708)

val finalEval = chainOfContinuations.run { finalResult =>
  Eval.later {
    println("Finished!")
    finalResult
  }
}

finalEval.value

// We used eval but much more likely to use something like IO
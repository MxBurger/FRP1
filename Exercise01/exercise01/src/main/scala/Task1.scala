package com.fhooe

import scala.collection.mutable
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

// a)
case class User(id: Int, name: String, email: String) {
  override def toString: String = s"User($id, $name, $email)"
}

// b)
var table = mutable.HashMap[Int, User]
  (1 -> User(1, "John", "john.doe@mail.com"),
    2 -> User(2, "Jane", "jane.doe@mail.com"),
    3 -> User(3, "Jack", "jacky.chanmail.com"),
    4 -> User(4, "", "jilly@bean.com"))

// c)
def fetchUserById(id: Int): Future[Try[User]] = {
  Future {
    val user = table.get(id)
    Thread.sleep(1000)
    user match {
      case Some(u) => Success(u)
      case None => Failure(new NoSuchElementException(s"User with ID $id not found"))
    }
  }
}

// d)
def validateUser(user: User): Boolean = {
  user.name.nonEmpty && user.email.nonEmpty && user.email.contains("@")
}

// e)
def fetchAndValidateUser(id: Int): Future[Try[User]] = {
  fetchUserById(id).map {
    case Success(user) =>
      if (validateUser(user)) Success(user)
      else Failure(new IllegalArgumentException(s"User $user is invalid"))
    case Failure(ex) => Failure(ex)
  }
}

// f)
def printResult(id: Int): Unit = {
  val future = fetchAndValidateUser(id)
  future.onComplete {
    case Success(user) => println(s"User fetched: $user")
    case Failure(ex) => println(s"Error: ${ex.getMessage}")
  }
}

object Task1 extends App {
  printResult(1)
  printResult(2)
  printResult(3)
  printResult(4)
  Thread.sleep(2000) // wait for the futures to complete
  println("Done")
}

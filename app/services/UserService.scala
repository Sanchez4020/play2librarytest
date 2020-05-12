package services

import javax.inject._
import models.{User, Users}

import scala.concurrent.Future

class UserService @Inject() (users: Users) {

  def addUser(user: User): Future[String] = {
    users.add(user)
  }

  def deleteUser(id: Long): Future[Int] = {
    users.delete(id)
  }

  def getUser(id: Long): Future[Option[User]] = {
    users.get(id)
  }

  def listAllUsers: Future[Seq[User]] = {
    users.listAll
  }

  def validateUser(login: String, password: String): Future[Seq[User]] = {
    users.validate(login, password)
  }
}
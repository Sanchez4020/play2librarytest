package models

import javax.inject._
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

case class User(id: Long, name: String, email: String, password: String)

case class UserFormData(name: String, email: String, password: String)

object UserForm {
  val form = Form(
    mapping(
      "name"   ->  nonEmptyText,
      "email" -> email,
      "password"  -> nonEmptyText
    )(UserFormData.apply)(UserFormData.unapply)
  )
}

import slick.jdbc.MySQLProfile.api._

class UserTableDef(tag: Tag) extends Table[User](tag, "user") {

  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def name = column[String]("name")
  def email = column[String]("email")
  def password = column[String]("password")

  override def * =
    (id, name, email, password) <>(User.tupled, User.unapply)
}

class Users @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  val users = TableQuery[UserTableDef]

  def add(user: User): Future[String] = {
    dbConfig.db.run(users += user).map(res => "User successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(users.filter(_.id === id).delete)
  }

  def get(id: Long): Future[Option[User]] = {
    dbConfig.db.run(users.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[User]] = {
    dbConfig.db.run(users.result)
  }

  def validate(login: String, password: String): Future[Seq[User]] = {
      dbConfig.db.run(users.filter(_.email === login).filter(_.password === password).result)
  }
}

case class LoginFormData(login: String, password: String)

object LoginForm {
  val form = Form(
    mapping(
      "login" ->  nonEmptyText,
      "password"  ->  nonEmptyText
    )(LoginFormData.apply)(LoginFormData.unapply)
  )
}

case class RegisterFormData(name: String, email: String, password: String, repeat_password: String)

object RegisterForm {
  val form = Form(
    mapping(
      "name"            ->  nonEmptyText,
      "email"           ->  email,
      "password"        -> nonEmptyText,
      "repeat_password" -> nonEmptyText
    )(RegisterFormData.apply)(RegisterFormData.unapply)
  )
}

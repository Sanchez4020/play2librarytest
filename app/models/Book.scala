package models

import javax.inject._
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}


case class Book (id: Long, var name: String, var author: String, var content: String, var users_id: Long)

case class BookFormData(id: Long, name: String, author: String, content: String)

object BookForm {
  val form = Form(
    mapping(
      "id"        -> longNumber,
      "name"      -> nonEmptyText,
      "author"    -> nonEmptyText,
      "content"   -> nonEmptyText,
    )(BookFormData.apply)(BookFormData.unapply)
  )
}

case class DeleteBooksFormData(ids: Seq[String])

object DeleteBooksForm {
  val form = Form(
    mapping(
      "ids" -> seq(text)
    )(DeleteBooksFormData.apply)(DeleteBooksFormData.unapply)
  )
}

import slick.jdbc.MySQLProfile.api._

class BookTableDef(tag: Tag) extends Table[Book](tag, "book") {

  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def name = column[String]("name")
  def author = column[String]("author")
  def content = column[String]("content")
  def users_id = column[Long]("users_id")

  override def * =
    (id, name, author, content, users_id) <>(Book.tupled, Book.unapply)
}

class Books @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  val books = TableQuery[BookTableDef]

  def add(book: Book): Future[String] = {
    dbConfig.db.run(books returning books.map(_.id) += book).map(res => "Book successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(books.filter(_.id === id).delete)
  }

  def deleteBooks(books_ids: Set[Long]): Future[Int] = {
    dbConfig.db.run(books.filter(_.id.inSet(books_ids)).delete)
  }

  def get(id: Long): Future[Option[Book]] = {
    dbConfig.db.run(books.filter(_.id === id).result.headOption)
  }

  def update(book: Book): Future[Int] = {
    dbConfig.db.run(books.insertOrUpdate(book));
  }

  def listForUser(users_id: Long): Future[Seq[Book]] = {
    dbConfig.db.run(books.filter(_.users_id === users_id).result)
  }

  def listAll: Future[Seq[Book]] = {
    dbConfig.db.run(books.result)
  }
}





package services

import javax.inject.Inject
import models.{Book, Books}

import scala.concurrent.Future

class BookService @Inject() (books: Books){

  def addBook(book: Book): Future[String] = {
    books.add(book)
  }

  def deleteBook(id: Long): Future[Int] = {
    books.delete(id)
  }

  def deleteBooks(books_ids: Set[Long]) = {
    books.deleteBooks(books_ids);
  }

  def getBook(id: Long): Future[Option[Book]] = {
    books.get(id)
  }

  def updateBook(book: Book): Future[Int] = {
    books.update(book);
  }

  def listForUser(users_id: Long): Future[Seq[Book]] = {
    books.listForUser(users_id)
  }

  def listAllBooks: Future[Seq[Book]] = {
    books.listAll
  }
}

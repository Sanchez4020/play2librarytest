package controllers

import java.io.File
import java.nio.file.Paths
import java.util.Calendar

import javax.inject._
import models.{Book, BookForm, BookFormData, Books, DeleteBooksForm}
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.libs.Files
import play.api.mvc._
import services.BookService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class BooksController @Inject()(cc: ControllerComponents, bookService: BookService) extends AbstractController(cc) {

  def addNewBook() = Action.async { implicit request: Request[AnyContent] =>
    request.session
      .get("users_id").map { users_id =>
      bookService.listForUser(users_id.toLong) map { books =>
        Ok(views.html.book(BookFormData(0, "", "", ""), "addBook"))
      }
    }
      .getOrElse {
        Future{ Redirect(routes.UserController.validateUser()) }
      }
  }

  def showBook(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    request.session
      .get("users_id").map { users_id =>
        bookService.getBook(id).map { books =>
          var name = ""
          var author = ""
          var content = ""
          for(book <- books) {
            name = book.name
            author = book.author
            content = book.content
          }
          Ok(views.html.book(BookFormData(id, name, author, content), "updateBook"))
        }
      }
      .getOrElse {
        Future{ Redirect(routes.UserController.validateUser()) }
      }
  }

  def addBook() = Action.async { implicit request: Request[AnyContent]  =>
    BookForm.form.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.index()))
      },
      data => {
        val newBook = Book(0, data.name, data.author, data.content, request.session.get("users_id").get(0).toString().toLong)
        bookService.addBook(newBook).map(_ => Redirect(routes.UserController.index()).withSession(request.session))
      }
    )
  }

  def updateBook() = Action.async { implicit request: Request[AnyContent]  =>
    BookForm.form.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.index()))
      },
      data => {
        val newBook = Book(data.id, data.name, data.author, data.content, request.session.get("users_id").get(0).toString().toLong)
        bookService.updateBook(newBook).map(_ => Redirect(routes.UserController.index()).withSession(request.session))
      }
    )
  }

  def deleteBooks() = Action.async { implicit request: Request[AnyContent]  =>
    DeleteBooksForm.form.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.index()))
      },
      data => {
        val ids = data.ids
        bookService.deleteBooks(ids.map(_.toString.toInt.toLong).toSet).map(_ => Redirect(routes.UserController.index()).withSession(request.session))
      }
    )
  }

}



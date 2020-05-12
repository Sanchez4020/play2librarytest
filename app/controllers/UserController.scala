package controllers

import javax.inject._
import models.{BookForm, BookFormData, LoginForm, RegisterForm, User}
import play.api.Logging
import play.api.mvc._
import services.BookService
import services.UserService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class UserController @Inject()(cc: ControllerComponents, userService: UserService, bookService: BookService) extends AbstractController(cc)  {

  def logOut() = Action.async { implicit request: Request[AnyContent] =>
    request.session
      .get("users_id").map { users_id =>
        userService.listAllUsers map { users =>
          // Ok(views.html.login1()).withNewSession
          Redirect(routes.UserController.validateUser()).withNewSession
        }
      }
      .getOrElse {
        Future{ Redirect(routes.UserController.validateUser()).withNewSession }
      }
  }

  def registerUser() = Action.async { implicit request: Request[AnyContent] =>
    RegisterForm.form.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.register()))
      },
      data => {
        if(!data.repeat_password.==(data.password)) {
          Future.successful(Ok(views.html.register(data, "Пароли не совпадают!")))
        }
        else {
          val newUser = User(0, data.name, data.email, data.password)
          userService.addUser(newUser).map( _ => Redirect(routes.UserController.validateUser()))
        }
      }
    )
  }

  def index() = Action.async { implicit request: Request[AnyContent] =>
    request.session
      .get("users_id").map { users_id =>
        bookService.listForUser(users_id.toLong) map { books =>
          Ok(views.html.index(books))
        }
      }
      .getOrElse {
        Future{ Redirect(routes.UserController.validateUser()) }
      }
  }

  def validateUser() = Action.async { implicit request: Request[AnyContent] =>
    LoginForm.form.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.login1()))
      },
      data => {
        println(data)
        userService.validateUser(data.login, data.password).map { res =>
          if (!res.length.>(0)) {
            Redirect(routes.UserController.validateUser())
          } else {
            var user = res.seq(0)
            Redirect(routes.UserController.index()).withSession(request.session + ("users_id" -> user.id.toString()) + ("user_name"  -> user.name))
          }
        }
      }
    )
  }

  def deleteUser(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    userService.deleteUser(id) map { res =>
      Redirect(routes.UserController.index())
    }
  }

}
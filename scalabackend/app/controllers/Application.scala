package controllers

import javax.inject.Inject

import dao.{ UsersDAO, TasksDAO }
import models.Task
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.I18nSupport
import play.api.mvc.{ AbstractController, ControllerComponents, Flash, RequestHeader }
import views.html
import models._
import scala.concurrent.Future

import scala.concurrent.ExecutionContext


case class LoginData(username: String, password: String)

class Application @Inject() (
    usersDAO: UsersDAO,
    tasksDAO: TasksDAO,
    controllerComponents: ControllerComponents
)(implicit executionContext: ExecutionContext) extends AbstractController(controllerComponents) with I18nSupport {

    val loginForm = Form(mapping(
        "Username" -> text(4, 10),
        "Password" -> text(8)
    )(LoginData.apply)(LoginData.unapply))

    val LOGIN = routes.Application.login
    val TASKLIST = routes.Application.taskList

    def login = Action { implicit request =>
        Ok(views.html.login(loginForm))
    }

    def validateLoginPost = Action.async { implicit request =>
        val loginInfo = request.body.asFormUrlEncoded
        loginInfo.map { args =>

            val username = args("username").head
            val password = args("password").head

            usersDAO.validateUser(username, password).map {isValid =>
                if (isValid) {
                    Redirect(TASKLIST).withSession("username" -> username)
                } else {
                    Redirect(LOGIN).flashing("error" -> "Invalid username/password.")
                }
            }
        }.getOrElse(Future.successful(Redirect(LOGIN)))
    }

    def validateCreateForm = Action.async { implicit request =>
        loginForm.bindFromRequest().fold(
            formWithError => Future.successful(BadRequest(views.html.login(formWithError))),
            ld =>
                usersDAO.insert(User(None, ld.username, ld.password)).map { _ =>
                    Redirect(TASKLIST).withSession("username" -> ld.username)
            }
        )
    }

    def logout = Action {
        Redirect(LOGIN).withNewSession
    }

    def taskList = Action.async { implicit request =>
        val usernameOption = request.session.get("username")
        usernameOption.map { username =>
            usersDAO.userIdFromName(username).flatMap {
                case Some(userId) =>
                    tasksDAO.list(userId).map { taskList =>
                        Ok(views.html.taskList(taskList))
                    }
                case None =>
                    Future.successful(Redirect(LOGIN))
            }
        }.getOrElse(Future.successful(Redirect(LOGIN)))
    }




    def addTask = Action.async { implicit request =>
        val usernameOption = request.session.get("username")
        usernameOption.map { username =>
            val postVals = request.body.asFormUrlEncoded
            postVals.map { args =>
                val taskText = args("NewTaskInput").head
                usersDAO.userIdFromName(username).flatMap {
                    case Some(userId) =>
                        val newTask = Task(None, taskText, Some(userId))
                        tasksDAO.insert(newTask).map { _ =>
                            Redirect(TASKLIST)
                        }
                    case None =>
                        Future.successful(Redirect(LOGIN))
                }
            }.getOrElse(Future.successful(Redirect(TASKLIST)))
        }.getOrElse(Future.successful(Redirect(LOGIN)))
    }

def deleteTask = Action.async { implicit request =>
    val usernameOption = request.session.get("username")
    usernameOption.map { username =>
        val postVals = request.body.asFormUrlEncoded
        postVals.map { args =>
            val index = args("index").head.toInt
            usersDAO.userIdFromName(username).flatMap {
                case Some(userId) =>
                    tasksDAO.delete(index).map { _ =>
                        Redirect(TASKLIST)
                    }
                case None =>
                    Future.successful(Redirect(LOGIN))
            }
        }.getOrElse(Future.successful(Redirect(TASKLIST)))
    }.getOrElse(Future.successful(Redirect(LOGIN)))
}

    def info() = Action { implicit request =>
        Ok(views.html.info(""))
    }

    def recoverPassword = Action {
        Ok("Very very sorry...")
    }
}
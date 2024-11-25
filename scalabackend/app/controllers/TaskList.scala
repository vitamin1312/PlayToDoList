package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import models._

case class LoginData(username: String, password: String)

@Singleton
class TaskList @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

    def taskList = Action { implicit request =>
        val usernameOption = request.session.get("username")
        usernameOption.map { username =>
            val tasks = TaskModel.getTasks(username)
            Ok(views.html.taskList(tasks))
        }.getOrElse(
            Redirect(routes.UsersController.login)
        )
    }

    def addTask = Action { implicit request =>

        val usernameOption = request.session.get("username")
        usernameOption.map { username =>

            val postVals = request.body.asFormUrlEncoded

            postVals.map { args =>
                val task = args("NewTaskInput").head
                TaskModel.addTask(username, task)
                Redirect(routes.TaskList.taskList)
            }.getOrElse(Redirect(routes.TaskList.taskList))
        }.getOrElse(Redirect(routes.UsersController.login))
    }

    def deleteTask = Action { implicit request =>

        val usernameOption = request.session.get("username")
        usernameOption.map { username =>

            val postVals = request.body.asFormUrlEncoded

            postVals.map { args =>
                val index = args("index").head.toInt
                TaskModel.removeTask(username, index)
                Redirect(routes.TaskList.taskList)
            }.getOrElse(Redirect(routes.TaskList.taskList))
        }.getOrElse(Redirect(routes.UsersController.login))
    }
}
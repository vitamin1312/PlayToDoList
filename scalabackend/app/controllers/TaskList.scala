package controllers

import javax.inject._
import play.api.mvc._
import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import models._


case class LoginData(username: String, password: String)

@Singleton
class TaskList @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider,
    val controllerComponents: ControllerComponents
    )(implicit ec: ExecutionContext)
    extends BaseController with HasDatabaseConfigProvider[JdbcProfile] {

    private val tasksModel = new TaskModel(db)

    def taskList = Action { implicit request =>
        val usernameOption = request.session.get("username")
        usernameOption.map { username =>
            val tasks = tasksModel.getTasks(username)
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
                tasksModel.addTask(username, task)
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
                tasksModel.removeTask(username, index)
                Redirect(routes.TaskList.taskList)
            }.getOrElse(Redirect(routes.TaskList.taskList))
        }.getOrElse(Redirect(routes.UsersController.login))
    }
}
package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import models._

@Singleton
class TaskList @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

    def taskList = Action {
        val username = "a"
        val tasks = TaskModel.getTasks(username)
        Ok(views.html.taskList(tasks))
    }
}
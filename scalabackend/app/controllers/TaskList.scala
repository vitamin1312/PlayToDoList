package controllers

import javax.inject._
import play.api._
import play.api.mvc._

@Singleton
class TaskList @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

    def taskList = Action {
        val tasks = List("a", "b", "v")
        Ok(views.html.taskList(tasks))
    }
}
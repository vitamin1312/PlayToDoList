package controllers

import javax.inject._
import play.api._
import play.api.mvc._

@Singleton
class UsersController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

    def login = Action {
        Ok(views.html.login())
    }

    def validateLoginPost = Action { request =>
        val loginInfo = request.body.asFormUrlEncoded
        loginInfo.map { args =>

            val username = args("username").head
            val password = args("password").head

            Redirect(routes.TaskList.taskList)
        }.getOrElse(Redirect(routes.UsersController.login))
    }
}
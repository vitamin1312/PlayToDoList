package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models._

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

            if (UserModel.validateUser(username, password)) {
                Redirect(routes.TaskList.taskList)
            } else {
                Redirect(routes.UsersController.login)
            }

            
        }.getOrElse(Redirect(routes.UsersController.login))
    }

    def createUserPost = Action { request =>
        val loginInfo = request.body.asFormUrlEncoded
        loginInfo.map { args =>

            val username = args("username").head
            val password = args("password").head

            if (UserModel.createUser(username, password)) {
                Redirect(routes.TaskList.taskList)
            } else {
                Redirect(routes.UsersController.login)
            }

            
        }.getOrElse(Redirect(routes.UsersController.login))
    }
}
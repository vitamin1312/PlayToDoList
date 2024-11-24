package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models._

@Singleton
class UsersController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

    def login = Action { implicit request =>
        Ok(views.html.login())
    }

    def validateLoginPost = Action { implicit request =>
        val loginInfo = request.body.asFormUrlEncoded
        loginInfo.map { args =>

            val username = args("username").head
            val password = args("password").head

            if (UserModel.validateUser(username, password)) {
                Redirect(routes.TaskList.taskList).withSession("username" -> username)
            } else {
                Redirect(routes.UsersController.login).flashing("error" -> "Invalid username/password.")
            }

            
        }.getOrElse(Redirect(routes.UsersController.login))
    }

    def createUserPost = Action { implicit request =>
        val loginInfo = request.body.asFormUrlEncoded
        loginInfo.map { args =>

            val username = args("username").head
            val password = args("password").head

            if (UserModel.createUser(username, password)) {
                Redirect(routes.TaskList.taskList).withSession("username" -> username)
            } else {
                Redirect(routes.UsersController.login).flashing("error" -> "User creation failed.")
            }

            
        }.getOrElse(Redirect(routes.UsersController.login))
    }

    def logout = Action {
        Redirect(routes.UsersController.login).withNewSession
    }
}
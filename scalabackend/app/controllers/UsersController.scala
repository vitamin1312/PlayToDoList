package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models._


@Singleton
class UsersController @Inject()(val controllerComponents: MessagesControllerComponents) extends MessagesBaseController {

    val loginForm = Form(mapping(
        "Username" -> text(4, 10),
        "Password" -> text(8)
    )(LoginData.apply)(LoginData.unapply))

    def login = Action { implicit request =>
        Ok(views.html.login(loginForm))
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

    def validateCreateForm = Action {implicit request =>
        loginForm.bindFromRequest.fold(
            formWithError => BadRequest(views.html.login(formWithError)),
            ld => 
                if (UserModel.createUser(ld.username, ld.password)) {
                    Redirect(routes.TaskList.taskList).withSession("username" -> ld.username)
                } else {
                    Redirect(routes.UsersController.login).flashing("error" -> "Invalid username/password.")
                }

        )        
    }

    def logout = Action {
        Redirect(routes.UsersController.login).withNewSession
    }
}
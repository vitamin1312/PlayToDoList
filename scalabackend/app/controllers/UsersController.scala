package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import javax.inject._
import scala.collection.mutable

import models._


@Singleton
class UsersController @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider,
    val controllerComponents: ControllerComponents
    )(implicit ec: ExecutionContext)
    extends BaseController with HasDatabaseConfigProvider[JdbcProfile] {

    private val userModel = new UserModel(db)

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

            if (userModel.validateUser(username, password)) {
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
                if (userModel.createUser(ld.username, ld.password)) {
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
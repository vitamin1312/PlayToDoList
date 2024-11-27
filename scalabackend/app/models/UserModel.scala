package models;

//import slick.jdbc.PostgresProfile.api._
import slick.jdbc.JdbcBackend
import scala.concurrent.ExecutionContext

import models.Tables._


class UserModel(db: JdbcBackend#Database)(implicit ec: ExecutionContext) {

    def validateUser(username: String, password: String): Boolean = {
        ???
    }

    def createUser(username: String, password: String): Boolean = {
        ???
    }
}
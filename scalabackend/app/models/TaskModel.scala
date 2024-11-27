package models

// import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcBackend

import models.Tables._


class TaskModel(db: JdbcBackend#Database)(implicit ec: ExecutionContext) {

    def getTasks(username: String): Seq[String] = {
        ???
    }
    def addTask(username: String, task: String): Unit = {
        ???
    }
    def removeTask(username: String, index: Int): Boolean = {
        ???
    }
}

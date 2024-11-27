package bootstrap

import java.text.SimpleDateFormat
import javax.inject.Inject

import dao.{ UsersDAO, TasksDAO }
import models.{ User, Task }

import scala.concurrent.{ Await, ExecutionContext }
import scala.concurrent.duration.Duration
import scala.util.Try

/** Initial set of data to be imported into the sample application. */
private[bootstrap] class InitialData @Inject() (
    usersDao: UsersDAO,
    tasksDao: TasksDAO
)(implicit executionContext: ExecutionContext) {

  def insert(): Unit = {
    val insertInitialDataFuture = for {
      count <- tasksDao.count() if count == 0
      _ <- usersDao.insertUsers(InitialData.users)
      _ <- tasksDao.insertTasks(InitialData.tasks)
    } yield ()

    Try(Await.result(insertInitialDataFuture, Duration.Inf))
  }

  insert()
}

private[bootstrap] object InitialData {

  def users = Seq(
    User(Option(1L), "admin", "admin"),
    User(Option(2L), "vitamin", "qwerty12"),
  )

  def tasks = Seq(
    Task(Option(1L), "code", Option(1L)),
    Task(Option(2L), "eat", Option(2L)),
    Task(Option(3L), "sleep", Option(2L)),

  )
}
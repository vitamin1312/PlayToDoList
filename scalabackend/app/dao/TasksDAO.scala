package dao

import javax.inject.{ Inject, Singleton }
import models.{ Task, User }
import scala.concurrent.{ ExecutionContext, Future }
import models.Task
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import slick.jdbc.JdbcProfile

import scala.concurrent.{ ExecutionContext, Future }

@Singleton()
class TasksDAO @Inject() (override protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends UsersDAO(dbConfigProvider)
    with HasDatabaseConfigProvider[JdbcProfile] {
        
    import profile.api._

    class Tasks(tag: Tag) extends Table[Task](tag, "TASKS") {
        def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
        def text = column[String]("TEXT")
        def userid = column[Long]("USERID")
        def * = (id.?, text, userid.?) <> (Task.tupled, Task.unapply _)
    }

    val tasks = TableQuery[Tasks]

    def count(): Future[Int] = {
        db.run(tasks.map(_.id).length.result)
    }

  
    def list(userid: Long): Future[Seq[(Task, User)]] = {

        val query = (for {
            (task, user) <- tasks joinLeft users on (_.userid === _.id)
            if task.userid === userid
            } yield (task, user.map(_.id), user.map(_.name), user.map(_.password)))

        for {
            totalRows <-  count()
            list = query.result.map { rows => rows.collect { case (computer, id, name, password) => (computer,
                User(id, name.getOrElse("None"), password.getOrElse("None"))) } }
            result <- db.run(list)
        } yield result
    }

    def insert(task: Task): Future[Unit] =
        db.run(tasks += task).map(_ => ())

    def insertTasks(tasks: Seq[Task]): Future[Unit] =
        db.run(this.tasks ++= tasks).map(_ => ())

    def delete(id: Long): Future[Unit] =
        db.run(tasks.filter(_.id === id).delete).map(_ => ())
}

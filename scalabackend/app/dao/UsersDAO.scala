package dao

import javax.inject.{ Inject, Singleton }

import scala.concurrent.{ ExecutionContext, Future }
import models.User
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import slick.jdbc.JdbcProfile

@Singleton()
class UsersDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {
        
    import profile.api._
    
    class Users(tag: Tag) extends Table[User](tag, "USERS") {
        def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
        def name = column[String]("NAME")
        def password = column[String]("PASSWORD")
        def * = (id.?, name, password) <> (User.tupled, User.unapply _)
    }

    val users = TableQuery[Users]

    def options(): Future[Seq[(String, String, String)]] = {
        val query = (for {
        user <- users
        } yield (user.id, user.name, user.password)).sortBy( /*id*/ _._1)

        db.run(query.result).map(rows => rows.map { case (id, text, userid) => (id.toString, text, userid.toString) })
    }

    def validateUser(username: String, password: String): Future[Boolean] = {
        val query = users.filter(user => user.name === username && user.password === password).exists
        db.run(query.result)
    }

    def insert(user: User): Future[Unit] =
        db.run(users += user).map(_ => ())

    def insertUsers(users: Seq[User]): Future[Unit] =
        db.run(this.users ++= users).map(_ => ())

    def userIdFromName(name: String) =  {
        val query = users.filter(user => user.name === name).map(_.id)
        db.run(query.result.headOption)
    }
}
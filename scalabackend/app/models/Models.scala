package models

case class Task(id: Option[Long], text: String, userid: Option[Long])
object Task {
    def unapply(t: Task): Option[(Option[Long], String, Option[Long])] = Some((t.id, t.text, t.userid))
    def tupled = (this.apply _).tupled
}

case class User(id: Option[Long], name: String, password: String)
object User {
    def unapply(u: User): Option[(Option[Long], String, String)] = Some((u.id, u.name, u.password))
    def tupled = (this.apply _).tupled
}
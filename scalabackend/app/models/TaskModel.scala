package models

object TaskModel {
    
    private var tasks = Map[String, List[String]]("a" -> List("eat", "sleep", "code"))

    def getTasks(username: String): Seq[String] = {
        tasks.get(username).getOrElse(Nil)
    }
    def addTask(username: String, task: String): Unit = ???
    def removeTask(username: String, index: Int): Boolean = ???
}

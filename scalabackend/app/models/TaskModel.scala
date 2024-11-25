package models

import collection.mutable


object TaskModel {
    
    private val tasks = mutable.Map[String, List[String]]("a" -> List("eat", "sleep", "code"))

    def getTasks(username: String): Seq[String] = {
        tasks.get(username).getOrElse(Nil)
    }
    def addTask(username: String, task: String): Unit = {
        tasks(username) = task :: tasks.get(username).getOrElse(Nil)
    }
    def removeTask(username: String, index: Int): Boolean = {
        if (index < 0
        || tasks.get(username).isEmpty 
        || index >= tasks(username).length) false
        else {
            tasks(username) = tasks(username).patch(index, Nil, 1)
                true
        }


    }
}

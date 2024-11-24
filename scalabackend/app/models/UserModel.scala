package models;

import collection.mutable._

object UserModel {

    private var users = Map[String, String]("a" -> "b")

    def validateUser(username: String, password: String): Boolean = {
        users.get(username).map(_ == password).getOrElse(false)
    }

    def createUser(username: String, password: String): Boolean = {
        if (users.contains(username)) false else {
            users(username) = password
            true
        }
    }
}
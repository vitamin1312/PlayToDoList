package models

object CodeGen extends App {
  slick.codegen.SourceCodeGenerator.run(
    "slick.jdbc.PostgresProfile",
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost/tasklist?user=admin&password=admin",
    "/home/vitamin1312/Documents/IT/PlayToDoList/scalabackend/app/",
    "models", None, None, true, false
  )
}
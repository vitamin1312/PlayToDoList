name := """ScalaBackend"""
organization := "GubkinUniversity"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.15"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test
libraryDependencies += "org.playframework" %% "play-slick" % "6.1.0"
libraryDependencies += "com.typesafe.slick" %% "slick-codegen" % "3.5.2"
libraryDependencies += "org.postgresql" % "postgresql" % "42.7.4"
libraryDependencies += "com.typesafe.slick" %% "slick-hikaricp" % "3.5.2"
libraryDependencies += "org.mindrot" % "jbcrypt" % "0.4"


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "GubkinUniversity.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "GubkinUniversity.binders._"

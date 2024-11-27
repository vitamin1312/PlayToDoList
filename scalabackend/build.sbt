name := """ScalaBackend"""
organization := "GubkinUniversity"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
.settings(
      scalaVersion := "2.13.15",
      scalacOptions ++= Seq(
        "-feature",
        "-Werror"
      ),
      libraryDependencies ++= Seq(
        guice,
        "org.playframework" %% "play-slick" % "6.1.1",
        "org.playframework" %% "play-slick-evolutions" % "6.1.1",
        "com.h2database" % "h2" % "2.3.232",
        specs2 % Test,
      ),
      (Global / concurrentRestrictions) += Tags.limit(Tags.Test, 1)
    )
    .settings((Test / javaOptions) += "-Dslick.dbs.default.connectionTimeout=30 seconds")
    // We use a slightly different database URL for running the slick applications and testing the slick applications.
    .settings((Test / javaOptions) ++= Seq("-Dconfig.file=conf/test.conf"))




// Adds additional packages into Twirl
//TwirlKeys.templateImports += "GubkinUniversity.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "GubkinUniversity.binders._"

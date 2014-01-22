import play.Project._

name := "computer-manager"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(jdbc, anorm, cache)

play.Project.playScalaSettings

scalaVersion := "2.10.3"

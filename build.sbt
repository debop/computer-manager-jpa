import play.Project._

name := "computer-manager"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
    cache,
    "kr.debop4s" % "debop4s-data" % "1.0-SNAPSHOT",
    "org.apache.tomcat" % "tomcat-jdbc" % "7.0.41" % "provided",
    "org.hsqldb" % "hsqldb" % "2.2.9" % "provided"
)

play.Project.playScalaSettings

scalaVersion := "2.10.3"

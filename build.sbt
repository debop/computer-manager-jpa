import play.Project._

name := "computer-manager"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
    cache,
    "org.ow2.asm" % "asm-all" % "4.2",
    "kr.debop4s" % "debop4s-core" % "1.0-SNAPSHOT",
    "kr.debop4s" % "debop4s-data" % "1.0-SNAPSHOT",
    //    "kr.hconnect" % "hconnect-core" % "1.3.6-SNAPSHOT",
    //    "kr.hconnect" % "hconnect-data" % "1.3.6-SNAPSHOT" exclude ("com.googlecode.xmemcached", "xmemcached"),
    "org.apache.tomcat" % "tomcat-jdbc" % "7.0.47",
    "org.hsqldb" % "hsqldb" % "2.3.0"
)

resolvers += "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository"

resolvers += "JBoss repository" at "https://repository.jboss.org/nexus/content/repositories"

resolvers += "Spring repository" at "http://maven.springframework.org/release"

transitiveClassifiers := Seq("sources")


play.Project.playScalaSettings

scalaVersion := "2.10.3"

name := "sfreechart"

version := "0.1"

organization := "vu.co.kaiyin"


scalaVersion := "2.11.7"

scalacOptions ++= Seq("-feature")

libraryDependencies += "org.jfree" % "jfreechart" % "1.0.19"


//publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))
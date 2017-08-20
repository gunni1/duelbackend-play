name := "duelbackend"

version := "1.0"

lazy val `duelbackend` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq( jdbc , cache , ws   , specs2 % Test )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

//Dependencies
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"

libraryDependencies += "org.mockito" % "mockito-all" % "1.8.4" % "test"

libraryDependencies += "com.typesafe.akka" % "akka-testkit_2.11" % "2.5.3" % "test"

libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.1.0"
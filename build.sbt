name := "library"
 
version := "1.0" 
      
lazy val `library` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq(ehcache , ws , specs2 % Test , guice )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

libraryDependencies ++= Seq(
  "org.webjars" % "material-design-lite" % "1.1.0"
)

// "org.webjars" % "material-design-icons" % "2.2.0",

// https://mvnrepository.com/artifact/org.webjars/material-design-lite
// libraryDependencies += "org.webjars" % "material-design-lite" % "1.1.0"

// https://mvnrepository.com/artifact/org.webjars/material-design-icons
// libraryDependencies += "org.webjars" % "material-design-icons" % "3.0.1"

// --- Sanchez 17.11.2019 ---
// libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.41"
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.19"

// --- Sanchez 21.11.2019 ---
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "4.0.2",
  "com.typesafe.play" %% "play-slick-evolutions" % "4.0.2"
)

// --- Sanchez 21.11.2019 ---
libraryDependencies += evolutions
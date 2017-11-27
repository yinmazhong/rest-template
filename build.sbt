
name := "bay"

version := "1.0"

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.0.10",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.10" % Test,
  "com.typesafe.akka" %% "akka-stream" % "2.5.4",
  "com.typesafe.akka" %% "akka-actor"  % "2.5.4",
  "io.spray" % "spray-json_2.11" % "1.3.4",
  "com.typesafe.akka" % "akka-http-spray-json_2.11" % "10.0.10"
)

mainClass in assembly := Some("bindong.WebServer")

assemblyJarName in assembly := "bay.jar"

/*enablePlugins(JettyPlugin)

containerPort := 1991*/

        
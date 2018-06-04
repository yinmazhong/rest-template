import com.typesafe.sbt.packager.MappingsHelper.{contentOf, directory}

name := "bay"

version := "1.04.11"

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.0.10",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.10" % Test,
  "com.typesafe.akka" %% "akka-stream" % "2.5.4",
  "com.typesafe.akka" %% "akka-actor"  % "2.5.4",
  "io.spray" % "spray-json_2.11" % "1.3.4",
  "com.typesafe.akka" % "akka-http-spray-json_2.11" % "10.0.10",
  "cn.epoque.aip"%%"cn-epoque-aip-common-log"%"1.0"
)

enablePlugins(JavaServerAppPackaging)

mainClass in Compile := Some("bindong.WebServer")

mappings in Universal ++= {
  // optional example illustrating how to copy additional directory
  directory("scripts") ++
    // copy configuration files to config directory
    contentOf("src/main/resources").toMap.mapValues("config/" + _)
}

// add 'config' directory first in the classpath of the start script,
// an alternative is to set the config file locations via CLI parameters
// when starting the application
scriptClasspath := Seq("../config/") ++ scriptClasspath.value

licenses := Seq(("CC0", url("http://creativecommons.org/publicdomain/zero/1.0")))

/*mainClass in assembly := Some("bindong.WebServer")

assemblyJarName in assembly := "bay.jar"*/

/*enablePlugins(JettyPlugin)

containerPort := 1991*/

        
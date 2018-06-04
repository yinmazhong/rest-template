
name := "bay"

version := "1.0"

scalaVersion := "2.11.11"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf-8")

unmanagedBase := baseDirectory.value / "lib"

libraryDependencies ++= {
  val akkaV = "2.3.9"
  val sprayV = "1.3.3"
  Seq(
    "io.spray" %% "spray-servlet" % sprayV,
    "io.spray" %% "spray-routing" % sprayV,
    "io.spray" %% "spray-testkit" % sprayV % "test",
    "io.spray" % "spray-json_2.11" % "1.3.2",
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV % "test",
    "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
    "org.json4s" % "json4s-native_2.11" % "3.5.0",
    "io.spray" % "spray-httpx_2.11" % "1.3.3",
    "org.apache.logging.log4j" % "log4j-api" % "2.8.2",
    "org.apache.logging.log4j" % "log4j-core" % "2.8.2",
    //"org.apache.logging.log4j" %% "log4j-api-scala" % "2.8.2"
    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
    //"org.specs2"          %%  "specs2-core"   % "2.3.7" % "test"
    "com.amazonaws" % "aws-java-sdk" % "1.11.140",
    "com.amazonaws" % "amazon-kinesis-client" % "1.7.5",
    "com.github.etaty" %% "rediscala" % "1.8.0",
    //"com.amazon" % "redshift-jdbc41" % "1.1.9.1009"
    //"kul" % "redshift-jdbc41" % "1.1.9.1009",
    //"ctx" % "redshift-jdbc41" % "1.1.9.1009"
    // https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient
    "org.apache.httpcomponents" % "httpclient" % "4.5.2",
    "net.debasishg" %% "redisclient" % "3.4"

  )
}

enablePlugins(JettyPlugin)

containerPort := 9896
        
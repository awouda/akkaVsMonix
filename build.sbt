
libraryDependencies += "io.monix" %% "monix" % "3.0.0-RC2"

libraryDependencies += "com.softwaremill.sttp" %% "core" % "1.5.1"
libraryDependencies += "com.softwaremill.sttp" %% "async-http-client-backend-monix" % "1.5.1"

libraryDependencies += "com.softwaremill.sttp" %% "akka-http-backend" % "1.5.2"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.11"


lazy val commonSettings = Seq(
  organization := "com.jtm",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.12.3",
)

lazy val monix = (project in file("."))
  .settings(
    commonSettings,
    name         := "monix"
  )

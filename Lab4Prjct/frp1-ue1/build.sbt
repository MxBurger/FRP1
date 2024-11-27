ThisBuild / version      := "1.0"
ThisBuild / scalaVersion := "3.6.1"
ThisBuild / scalacOptions := Seq("-unchecked", "-feature", "-deprecation")

val logbackVersion  = "1.5.12"

lazy val root = (project in file("."))
  .settings(
    name := "frp-ue1",
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % logbackVersion
    )
  )

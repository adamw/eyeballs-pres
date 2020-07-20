import sbt._
import Keys._

name := "eyeballs-pres"
organization := "com.softwaremill"
scalaVersion := "2.13.2"

val zioVersion = "1.0.0-RC21-2"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % zioVersion
)

commonSmlBuildSettings

import sbt._
import BuildConfig._

lazy val commonScalacOptions = Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:experimental.macros",
  "-language:postfixOps",
  "-unchecked",
  "-Ywarn-nullary-unit",
  "-Xfatal-warnings",
  "-Xlint:-unused",
  "-Ywarn-dead-code",
  "-Xfuture"
)

lazy val commonSettings = Seq(
  name := "sceleton",

  organization := "com.chrisbenincasa",

  version := "1.0" + s"-${BuildConfig.Revision.revision}",

  scalaVersion := "2.12.7",

  scalacOptions ++= commonScalacOptions,

  scalacOptions in doc ++= commonScalacOptions.filterNot(_ == "-Xfatal-warnings"),

  initialCommands := "import com.chrisbenincasa.tools.sceleton._"
) ++ Publishing.publishSettings

lazy val sceleton = (project in file(".")).
  settings(commonSettings).
  settings(
    name := "sceleton",
    aggregate in update := false,
    publish := {} // Don't publish root package
  ).aggregate(core)

lazy val core = (project in file("core")).
  settings(commonSettings).
  settings(
    name := "sceleton-core",
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % versions.logback,
      "ch.qos.logback" % "logback-core" % versions.logback,
      "com.github.scopt" %% "scopt" % versions.scopt,
      "org.jline" % "jline" % versions.jline,
      "com.github.spullara.mustache.java" % "compiler" % "0.9.5",
      "commons-io" % "commons-io" % "2.6",
      "org.apache.commons" % "commons-text" % "1.4",
      "org.eclipse.jgit" % "org.eclipse.jgit.pgm" % "3.7.0.201502260915-r",
      Dependencies.scalatest
    )

  )

packageOptions in (Compile, packageBin) += Package.ManifestAttributes( "Sceleton-Version" -> version.value )

publishMavenStyle := true

lazy val showVersion = taskKey[Unit]("Show version")

showVersion := {
  println(version.value)
}

// custom alias to hook in any other custom commands
addCommandAlias("build", "; compile")
addCommandAlias("install", "; assembly")

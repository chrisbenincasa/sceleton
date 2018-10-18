import sbt._

object BuildConfig {
  object versions {
    val jline = "3.3.1"
    val logback = "1.1.8"
    val scopt = "3.7.0"
  }

  object Dependencies {
    val scalatest = "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  }

  object Revision {
    lazy val revision = System.getProperty("revision", "SNAPSHOT")
  }
}
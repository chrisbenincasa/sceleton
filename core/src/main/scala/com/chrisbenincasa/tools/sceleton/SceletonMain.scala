package com.chrisbenincasa.tools.sceleton

import com.chrisbenincasa.tools.sceleton.core.SceletonRunner
import java.io.File
import scopt.OptionParser
import scala.util.{Failure, Success}

case class SceletonOptions(
  template: String = "",
  targetDir: File = new File("."),
  gitRef: Option[String] = None,
  prefilledProps: Map[String, Any] = Map.empty // Useful when interacting with sceleton directly in Scala
)

class Sceleton extends PrettyConsole {
  val parser: OptionParser[SceletonOptions] = new scopt.OptionParser[SceletonOptions]("sceleton") {
    head("sceleton", "0.0.1")

    arg[String]("<template>").text("The template to generate").action { (template, config) =>
      config.copy(template = template)
    }

    opt[File]("target").text("Absolute path of the target directory").optional().action { (target, config) =>
      config.copy(targetDir = target)
    }

    opt[String]("ref").text("The git ref to clone when scaffolding a template located remotely").optional().action { (ref, config) =>
      config.copy(gitRef = Some(ref))
    }

    help("help").text("Sup")

    note("""WELCOME
        |hi
      """.stripMargin
    )
  }

  def run(args: Array[String]): Int = {
    parser.parse(args, SceletonOptions()) match {
      case Some(config) =>
        new SceletonRunner(config).run() match {
          case Success(_) => 0
          case Failure(e) =>
            println("Failed")
            e.printStackTrace()
            1
        }
      case None =>
        "Could not parse args!".printRed()
        1
    }
  }
}

object SceletonMain extends Sceleton with App with PrettyConsole {
  "WELCOME TO YOUR WORST NIGHTMARE".printRed()
  System.exit(run(args))
}

trait PrettyConsole {
  implicit class RichString(str: String) {
    def printBlue() = {
      println(str.asBlue)
    }

    def printCyan() = {
      println(asColor(Console.CYAN))
    }

    def printRed() = {
      println(str.asRed)
    }

    def printYellow() = {
      println(str.asYellow)
    }

    def asColor(color: String): String = color + str + Console.RESET

    def asBlue = Console.BLUE + str + Console.RESET
    def asRed = Console.RED + str + Console.RESET
    def asYellow = Console.YELLOW + str + Console.RESET
  }
}

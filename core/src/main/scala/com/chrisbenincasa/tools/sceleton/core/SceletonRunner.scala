package com.chrisbenincasa.tools.sceleton.core

import com.chrisbenincasa.tools.sceleton.cli.Interactor
import com.chrisbenincasa.tools.sceleton.filesystem._
import com.chrisbenincasa.tools.sceleton.gen.mustache.MustacheRenderer
import com.chrisbenincasa.tools.sceleton.gen.{GenerateFileTree, IdentifyTemplateRoot, TreeRenderer}
import com.chrisbenincasa.tools.sceleton.git.GitCloner
import com.chrisbenincasa.tools.sceleton.util.Implicits._
import com.chrisbenincasa.tools.sceleton.{PrettyConsole, SceletonOptions}
import java.io.File
import org.apache.commons.io.FileUtils
import scala.util.Try
import scala.util.matching.Regex

object SceletonRunner {
  val PropertiesFile = "default.properties"
}

class SceletonRunner(config: SceletonOptions) extends PrettyConsole {
  import SceletonRunner._

  def run(): Try[Unit] = {
    for {
      workingDir <- createWorkingDirectory()
      _ <- Try(FileUtils.deleteDirectory(workingDir / ".git"))
      templateRootNode <- IdentifyTemplateRoot(workingDir)
      templateRoot = templateRootNode.file
      propertiesPath <- Try(BFS(templateRoot, _.file.getName == PropertiesFile).head)
      properties <- LoadProperties(propertiesPath.file)
      readProperties <- Interactor(properties, config.prefilledProps)
      _ <- GenerateFileTree(templateRoot, readProperties, MustacheRenderer)
      _ <- TreeRenderer(templateRoot, readProperties, MustacheRenderer)
      _ <- Copier(templateRoot, config.targetDir, force = false)
    } yield {}
  }

  private def createWorkingDirectory(): Try[File] = {
    parseTemplate(config.template).collect {
      case local: LocalFilePath => Copier(local, force = false)
      case repo: GithubPath => GitCloner(repo, config.gitRef)
    }.flatten
  }

  private def parseTemplate(template: String): Try[TemplateType] = {
    Try {
      template match {
        case Matches.Local(path) => LocalFilePath(path)
        case Matches.GitHub(user, repo) => GithubPath(user, repo)
        case _ => throw new IllegalArgumentException(s"Unsupported path format: $template")
      }
    }
  }

  object Matches {
    val GitHub: Regex = """^([^\s/]+)/([^\s/]+?)$""".r
    val Local: Regex = """^file://(\S+)$""".r
    val NativeUrl: Regex = "^(git[@|://].*)$".r
    val HttpsUrl: Regex = "^(https://.*)$".r
    val HttpUrl: Regex = "^(http://.*)$".r
    val SshUrl: Regex = "^(ssh://.*)$".r
  }
}

sealed trait TemplateType
case class LocalFilePath(path: String) extends TemplateType
case class GithubPath(user: String, repository: String) extends TemplateType {
  val httpUrl = s"https://github.com/$user/$repository.git"
  val privateUrl = s"git@github.com:$user/$repository.git"
}

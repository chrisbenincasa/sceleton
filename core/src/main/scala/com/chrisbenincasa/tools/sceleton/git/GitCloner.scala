package com.chrisbenincasa.tools.sceleton.git

import com.chrisbenincasa.tools.sceleton.core.GithubPath
import com.chrisbenincasa.tools.sceleton.git.auth.{ConsoleCredentialsProvider, OSXKeychainCredentialsProvider}
import com.chrisbenincasa.tools.sceleton.util.Implicits._
import com.chrisbenincasa.tools.sceleton.util.TempDirectory
import java.io.File
import org.eclipse.jgit.api.{Git => JGit}
import org.eclipse.jgit.transport.ChainingCredentialsProvider
import scala.util.Try

object GitCloner extends TempDirectory {
  val platform: String = System.getProperty("os.name").toLowerCase()

  def apply(gh: GithubPath, ref: Option[String]): Try[File] = {
    val tmp = nextTempDir
    Try {
      JGit.cloneRepository().
        setURI(gh.httpUrl).
        setDirectory(tmp).
        setBranch(ref.getOrElse("master")).
        setCredentialsProvider(
          new ChainingCredentialsProvider(
            Seq(OSXKeychainCredentialsProvider).filter(_ => platform.contains("mac")) ++
            Seq(ConsoleCredentialsProvider): _*
          )
        ).
        call().
        close()
    }.map(_ => tmp).through(f => println(s"cloned to $f"))
  }

  def apply(uri: String, dest: File = nextTempDir): Try[File] = {
    ???
  }
}

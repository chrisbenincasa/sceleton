package com.chrisbenincasa.tools.sceleton.filesystem

import com.chrisbenincasa.tools.sceleton.core.LocalFilePath
import com.chrisbenincasa.tools.sceleton.util.Implicits._
import com.chrisbenincasa.tools.sceleton.util.TempDirectory
import java.io.File
import org.apache.commons.io.FileUtils
import scala.util.{Failure, Try}

object Copier extends TempDirectory {

  def apply(localFilePath: LocalFilePath, force: Boolean): Try[File] = {
    apply(new File(localFilePath.path), force = force)
  }

  def apply(source: File, force: Boolean): Try[File] = {
    apply(source, nextTempDir, force)
  }

  def apply(source: File, dest: File, force: Boolean): Try[File] = {
    if (!source.exists) {
      Failure(new IllegalArgumentException(s"no file located at ${source.getAbsolutePath}"))
    } else if (!source.isDirectory) {
      Failure(new IllegalArgumentException(s"path $source doesn't reference a directory"))
    } else {
      Try(FileUtils.copyDirectory(source, dest)).
        map(_ => {
          new File(s"${dest.getAbsolutePath}")
        }).
        through(println)
    }
  }

  private def lastPathPart(f: File): String = {
    f.getAbsolutePath.split(File.separator).last
  }
}

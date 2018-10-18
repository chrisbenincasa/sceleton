package com.chrisbenincasa.tools.sceleton.filesystem

import java.io.File
import org.apache.commons.io.FileUtils
import scala.util.{Failure, Try}

object Mover {
  def apply(src: File, dest: File): Try[File] = {
    if (!src.exists) {
      Failure(new IllegalArgumentException(s"Attempted to move file: no file located at ${src.getAbsolutePath}"))
    } else if (src.isDirectory) {
      Try {
        FileUtils.moveDirectory(src, dest)
      }.map(_ => dest)
    } else {
      Try {
        FileUtils.moveFile(src, dest)
      }.map(_ => dest)
    }
  }
}

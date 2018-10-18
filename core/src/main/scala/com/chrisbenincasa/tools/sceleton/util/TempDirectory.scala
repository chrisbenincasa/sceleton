package com.chrisbenincasa.tools.sceleton.util

import java.io.File
import org.apache.commons.io.FileUtils

object TempDirectory {
  private val tmpStream = new Iterator[File] {
    override def hasNext: Boolean = true

    override def next(): File = new File(FileUtils.getTempDirectory, "sceleton_" + System.nanoTime)
  }
}

trait TempDirectory {
  def nextTempDir: File = TempDirectory.tmpStream.next()
}

package com.chrisbenincasa.tools.sceleton.util

import com.chrisbenincasa.tools.sceleton.filesystem.FsNode
import java.io.File

trait FileExtensions {
  implicit class RichFile(f: File) {
    def / (s: String): File = {
      val newPath = s"${f.getAbsolutePath.stripSuffix(File.separator)}${File.separator}$s"
      new File(newPath)
    }
  }

  implicit def fileToFsNode(f: File): FsNode = FsNode(f)
}

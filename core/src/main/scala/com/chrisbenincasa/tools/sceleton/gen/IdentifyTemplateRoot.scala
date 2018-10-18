package com.chrisbenincasa.tools.sceleton.gen

import com.chrisbenincasa.tools.sceleton.filesystem.{DFS, FsNode}
import java.io.File
import scala.util.{Success, Try}

object IdentifyTemplateRoot {
  val Sceleton = "sceleton"
  val isTemplateRoot: FsNode => Boolean = f => f.file.isDirectory && f.file.getAbsolutePath.endsWith(Sceleton)

  def apply(file: File): Try[FsNode] = {
    val node = FsNode(file)
    DFS.filter(node, isTemplateRoot) match {
      case Seq(x) => Success(x)
      case Seq(x, _ @ _*) => Success(x)
      case Seq() => Success(node)
    }
  }
}

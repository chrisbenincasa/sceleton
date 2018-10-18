package com.chrisbenincasa.tools.sceleton.filesystem

import java.io.File

object FsNode {
  def apply(f: File): FsNode = {
    if (f.isDirectory) {
      FsDirectory(f, () => Option(f.listFiles()).getOrElse(Array.empty).map(FsNode(_)))
    } else {
      FsFile(f)
    }
  }
}
sealed trait FsNode {
  def file: File
}

case class FsDirectory(file: File, children: () => Seq[FsNode]) extends FsNode {
  override def equals(obj: scala.Any): Boolean = {
    obj match {
      case FsDirectory(d2, _) => file.getAbsolutePath == d2.getAbsolutePath
      case _ => false
    }
  }
}
case class FsFile(file: File) extends FsNode {
  override def equals(obj: scala.Any): Boolean = {
    obj match {
      case FsFile(f2) => file.getAbsolutePath == f2.getAbsolutePath
      case _ => false
    }
  }
}

package com.chrisbenincasa.tools.sceleton.gen

import com.chrisbenincasa.tools.sceleton.filesystem.LoadProperties.Props
import com.chrisbenincasa.tools.sceleton.filesystem.{FsNode, TreeProcess}
import java.io.File
import scala.util.Try
import com.chrisbenincasa.tools.sceleton.util.Implicits._

trait Renderer {
  def apply(file: File, properties: Props): Try[File]

  def apply(string: String, properties: Props): Try[String]
}

object TreeRenderer {
  def apply(file: File, props: Props, renderer: Renderer): Try[Unit] = {
    Try {
      TreeProcess(file, _.file.isFile, f => renderer(f.file, props).map(FsNode(_)))
    }
  }
}

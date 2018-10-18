package com.chrisbenincasa.tools.sceleton.gen

import com.chrisbenincasa.tools.sceleton.filesystem.LoadProperties.Props
import com.chrisbenincasa.tools.sceleton.filesystem.{DFS, FsNode, Mover}
import com.chrisbenincasa.tools.sceleton.util.Implicits._
import java.io.File
import scala.util.Try

object GenerateFileTree {
  val fileIsTemplatable: FsNode => Boolean = f => {
    val name = f.file.getName
    name.contains("{{") && name.contains("}}")
  }

  def apply(root: FsNode, props: Props, renderer: Renderer): Try[Unit] = apply(root.file, props, renderer)

  def apply(root: File, props: Props, renderer: Renderer): Try[Unit] = {
    // Apply tree formatting with DFS so child directory move operations use the right paths.
    Try {
      DFS(FsNode(root), fileIsTemplatable, node => {
        for {
          newFileName <- renderer(node.file.getName, props)
          res <- Mover(node.file, node.file.getParentFile / newFileName)
        } yield res
      })
    }.map(_ => {})
  }
}

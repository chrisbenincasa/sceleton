package com.chrisbenincasa.tools.sceleton.filesystem

import java.io.File
import scala.util.{Success, Try}

object TreeProcess {
//  def apply(root: File, process: File => Try[Unit]): Try[Unit] = {
//    apply(root, _ => true, process)
//  }
//
//  def apply(root: File, filter: File => Boolean, process: File => Try[Unit]): Try[Unit] = {
//    def walk(dir: File): Try[Unit] = {
//      Option(dir.listFiles()).getOrElse(Array.empty).foldLeft(Success(Unit): Try[Unit]) {
//        case (t, f) =>
//          t.flatMap {
//            case _ if f.isDirectory =>
//              val dirProcTry: Try[Unit] = if (filter(f)) process(f) else Success(Unit)
//              dirProcTry.flatMap(_ => walk(f))
//            case _ => if (filter(f)) process(f) else Success(Unit)
//          }
//      }
//    }
//
//    Try(require(root.isDirectory)).flatMap(_ => walk(root))
//  }

  def apply(root: FsNode, filter: FsNode => Boolean, process: FsNode => Try[FsNode], dfs: Boolean = true) = {
    if (dfs) {
      DFS(root, filter, process)
    } else {
      BFS(root, filter, process)
    }
  }
}

object TreeTraverse {
  def apply(root: FsNode, onNode: FsNode => Unit): Unit = {
    def recur(curr: FsNode): Unit = {
      curr match {
        case node @ FsDirectory(_, children) => {
          onNode(node)
          children().foreach(recur)
        }
        case node @ FsFile(_) => onNode(node)
      }
    }

    recur(root)
  }
}

object TreeMap {
  def apply(root: FsNode, map: File => File): FsNode = {
    def recur(curr: FsNode): FsNode = {
      curr match {
        case FsDirectory(dir, children) => FsDirectory(map(dir), () => children().map(recur))
        case FsFile(file) => FsFile(map(file))
      }
    }

    recur(root)
  }
}

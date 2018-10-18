package com.chrisbenincasa.tools.sceleton.filesystem

import scala.util.{Success, Try}

object BFS {
  val IgnoredDirectories = Set(".git")

  def pure(root: FsNode): Seq[FsNode] = apply(root)

  def filter(root: FsNode, filter: FsNode => Boolean): Seq[FsNode] = apply(root, filter)

  def apply(root: FsNode, filter: FsNode => Boolean = _ => true, process: FsNode => Try[FsNode] = Success(_)): Seq[FsNode] = {
    val processAndReport: FsNode => Option[FsNode] = f => {
      val v = process(f)
      if (v.isFailure) Console.err.println(s"Process failed on node: $f\n${v.failed.get.getMessage}")
      v.toOption
    }

    def dfs(curr: FsNode, acc: Seq[FsNode] = Seq()): Seq[FsNode] = {
      curr match {
        case FsDirectory(dir, _) if IgnoredDirectories(dir.getName) => Seq()
        case FsDirectory(_, children) =>
          children().foldLeft(acc) {
            case (results, dir2: FsDirectory) if results.contains(dir2) => results
            case (results, dir2: FsDirectory) => (Seq(curr).filter(filter).flatMap(processAndReport(_)) ++ dfs(dir2, results)).distinct
            case (results, f: FsFile) if results.contains(f) => results
            case (results, f: FsFile) => dfs(f, results)
          }
        case f @ FsFile(_) => (Seq(f).filter(filter).flatMap(processAndReport(_)) ++ acc).distinct
      }
    }

    dfs(root)
  }
}

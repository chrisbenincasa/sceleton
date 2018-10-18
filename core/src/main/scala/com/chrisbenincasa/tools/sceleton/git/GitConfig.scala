package com.chrisbenincasa.tools.sceleton.git

import java.io.File
import org.eclipse.jgit.lib.Config
import org.eclipse.jgit.storage.file.FileBasedConfig
import org.eclipse.jgit.util.FS

object GitConfig {
  lazy val conf = {
    new Config()
    val c = new FileBasedConfig(new File(s"${System.getenv("HOME")}/.gitconfig"), FS.DETECTED)
    c.load()
    c
  }

  lazy val username = conf.getString("github", null, "user")
}

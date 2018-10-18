package com.chrisbenincasa.tools.sceleton.git.auth

import com.chrisbenincasa.tools.sceleton.git.GitConfig
import org.eclipse.jgit.transport.{CredentialItem, CredentialsProvider, URIish}
import scala.sys.process._

object OSXKeychainCredentialsProvider extends CredentialsProvider {
  override def isInteractive: Boolean = false

  override def supports(items: CredentialItem*): Boolean = true

  override def get(uri: URIish, items: CredentialItem*): Boolean = {
    items.foreach {
      case i: CredentialItem.Username =>
        i.setValue(GitConfig.username)

      case i: CredentialItem.Password =>
        val username = items.collectFirst { case username: CredentialItem.Username => username }
        // Relies on the fact that username is asked for first...
        username.foreach(u => {
          val pwd = s"security find-internet-password -a ${u.getValue} -s github.com -g -w".!!.
            trim().
            replaceAll("\\n", "")

          i.setValueNoCopy(pwd.toCharArray)
        })
      case i: CredentialItem.InformationalMessage => println(i.getPromptText)
      case i: CredentialItem.StringType => println(i.getPromptText)
    }

    true
  }
}

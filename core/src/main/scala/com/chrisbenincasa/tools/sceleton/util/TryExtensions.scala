package com.chrisbenincasa.tools.sceleton.util

import scala.util.Try

trait TryExtensions {
  implicit class RichTry[T](t: Try[T]) {
    def through(f: T => Unit): Try[T] = t.map(x => { f(x); x })
  }
}

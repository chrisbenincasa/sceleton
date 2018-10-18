package com.chrisbenincasa.tools.sceleton.gen.mustache

import com.github.mustachejava.{DefaultMustacheFactory, ObjectHandler}

class CustomMustacheFactory extends DefaultMustacheFactory {
  private val objectHandler = new CustomObjectHandler

  override def getObjectHandler: ObjectHandler = objectHandler
}

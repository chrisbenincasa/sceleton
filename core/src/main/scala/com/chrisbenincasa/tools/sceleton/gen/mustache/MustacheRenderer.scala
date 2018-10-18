package com.chrisbenincasa.tools.sceleton.gen.mustache

import com.chrisbenincasa.tools.sceleton.filesystem.LoadProperties.Props
import com.chrisbenincasa.tools.sceleton.gen.Renderer
import java.io._
import scala.collection.JavaConverters._
import scala.util.Try

object MustacheRenderer extends Renderer {
  private val mustacheFactory = new CustomMustacheFactory()

  def apply(file: File, properties: Props): Try[File] = {
    Try {
      val stache = mustacheFactory.compile(new FileReader(file), file.getAbsolutePath)

      stache.execute(new FileWriter(file), properties.asJava).flush()

      file
    }
  }

  def apply(string: String, properties: Props): Try[String] = {
    Try {
      val writer = new StringWriter()
      val stache = mustacheFactory.compile(new StringReader(string), "stringRender")

      stache.execute(writer, properties.asJava).flush()

      writer.toString
    }
  }
}

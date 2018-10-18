package com.chrisbenincasa.tools.sceleton.filesystem

import java.io.{File, FileReader}
import java.util.Properties
import scala.collection.JavaConverters._
import scala.util.Try

object LoadProperties {
  type Props = Map[String, Any]
  object Props {
    def empty: Props = Map.empty
  }

  type Transform = String => String

  def apply(f: File): Try[Props] = {
    Try {
      val props = new Properties()
      props.load(new FileReader(f))
      props
    }.flatMap(parseLoadedProperties)
  }

  def parseLoadedProperties(properties: Properties): Try[Props] = {
    Try {
      properties.asScala.mapValues(_.trim).flatMap {
        case (k, v) if stringIsNumeric(v) => Seq(k -> new java.lang.Double(v.toDouble))
        case (k, v) if stringIsBoolean(v) => Seq(k -> new java.lang.Boolean(v))
        case (k, v) if stringIsSequence(v) => Seq(k -> v.split(",").toList.asJava)
        case (k, v) => Seq(k -> v.toString)
      }.toMap
    }
  }

  private def stringIsBoolean(s: String) = Try(s.toBoolean).isSuccess

  private def stringIsNumeric(s: String) = Try(s.toDouble).isSuccess

  private def stringIsSequence(s: String) = s.trim.split(",").length > 1
}

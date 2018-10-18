package com.chrisbenincasa.tools.sceleton.gen

import org.apache.commons.lang3.StringUtils
import org.apache.commons.text.CaseUtils
import scala.util.Random

object StringTransforms {
  val FormatTransform = "format"

  val TransformDeclarationSeparator = "="
  val Delimiter = ';'
  val TransformSeparator = ','

  val PathDelimiter = "__"

  val r = new Random()

  val camelFunc: Boolean => String => String = firstLetterCap => s => CaseUtils.toCamelCase(s, firstLetterCap)
  val underscoreFunc: String => String = StringUtils.replaceAll(_, " ", "_")
  val packagedFunc: String => String = s => s.replaceAll("\\.", "\\/")
  val capitalizeFunc: String => String = _.capitalize
  val decapitalizeFunc: String => String = StringUtils.uncapitalize
  val lowercaseFunc: String => String = _.toLowerCase()
  val uppercaseFunc: String => String = _.toUpperCase()
  val startCaseFunc: String => String =
    ((s: String) => StringUtils.lowerCase(s)).
      andThen(StringUtils.split(_, " ")).
      andThen(_.map(StringUtils.capitalize)).
      andThen(_.mkString(" "))
  val hyphenateFunc: String => String = StringUtils.replace(_, " ", "-")
  val normalizeFunc: String => String = lowercaseFunc.andThen(hyphenateFunc)
  val wordOnlyFunc: String => String = _.replaceAll("\\W", "")

  val TransformGroups: Seq[(Seq[String], String, String => String)] = Seq(
    (Seq("camel"), "", camelFunc(false)),
    (Seq("Camel"), "", camelFunc(true)),
    (Seq("start", "start-case"), "", startCaseFunc),
    (Seq("underscore", "snake", "snake-case"), "Replace dots and spaces with underscore", underscoreFunc),
    (Seq("hyphenate", "hyphen"), "", hyphenateFunc),
    (Seq("normalize", "norm"), "", normalizeFunc),
    (Seq("packaged", "package-dir"), "Replace dots with slashes", packagedFunc),
    (Seq("upper", "uppercase"), "Uppercase entire string", uppercaseFunc),
    (Seq("lower", "lowercase"), "Lowercase entire string", lowercaseFunc),
    (Seq("cap", "capitalize"), "Uppercase first letter", capitalizeFunc),
    (Seq("decap", "decapitalize"), "Lowercase first letter", decapitalizeFunc),
    (Seq("word", "word-only"), "", wordOnlyFunc),
    (Seq("random", "generate-random"), "", _ + r.alphanumeric.take(r.nextInt(10)).mkString(""))
  )

  val Transforms: Map[String, String => String] = TransformGroups.flatMap {
    case (names, _, func) => names.map(_ -> func)
  }.toMap

  def parseTransforms(transforms: List[String]): Map[String, List[String]] = {
    transforms.map(t => {
      t.split(TransformDeclarationSeparator) match {
        case Array(name, value) => name -> unquote(value).split(TransformSeparator).toList.map(_.trim).filter(_.nonEmpty)
        case arr => throw new IllegalArgumentException(s"Invalid transforms array: ${arr.toList}")
      }
    }).toMap
  }

  def genTransformFunc(ts: Seq[String]): String => String = {
    ts.foldLeft(identity[String](_))((f, in) => f.andThen(Transforms.getOrElse(in, identity(_))))
  }

  def unquote(s: String): String = s.replaceAll("^\"|\"$", "")
}

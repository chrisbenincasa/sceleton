package com.chrisbenincasa.tools.sceleton.gen.mustache

import com.chrisbenincasa.tools.sceleton.gen.StringTransforms
import com.github.mustachejava.reflect.ReflectionObjectHandler
import com.github.mustachejava.util.Wrapper
import com.github.mustachejava.{Binding, Code, TemplateContext, TemplateFunction}
import java.util
import scala.collection.JavaConverters._

class CustomObjectHandler extends ReflectionObjectHandler { self =>
  protected lazy val TransformFuncSeparator = "_"
  protected lazy val TransformFuncSuffix = "Func"

  override def createBinding(name: String, tc: TemplateContext, code: Code): Binding = {
    super.createBinding(name, tc, code)
  }

  override def find(name: String, scopes: util.List[AnyRef]): Wrapper = {
    val (newName, newScopes) = name.split(StringTransforms.Delimiter).toList match {
      case origName :: xs if StringTransforms.parseTransforms(xs).contains(StringTransforms.FormatTransform) =>
        val transforms = StringTransforms.parseTransforms(xs)(StringTransforms.FormatTransform)
        val suffix = transforms.mkString(TransformFuncSeparator) + TransformFuncSuffix
        val newName = origName + suffix
        val additionalScope = Map(
          newName -> new TemplateFunction {
            private val transform = StringTransforms.genTransformFunc(transforms)
            override def apply(t: String): String = {
              val orig = self.find(origName, scopes).call(scopes)
              transform(orig.toString)
            }
          }
        ).asJava

        val newScopes = scopes.asScala += additionalScope
        newName -> newScopes.asJava
      case origName :: _ => origName -> scopes
      case Nil => name -> scopes
    }

    super.find(newName, newScopes)
  }
}

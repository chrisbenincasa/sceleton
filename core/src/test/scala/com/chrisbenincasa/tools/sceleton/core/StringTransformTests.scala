package com.chrisbenincasa.tools.sceleton.core

import com.chrisbenincasa.tools.sceleton.gen.StringTransforms
import org.scalatest.{FreeSpec, Matchers}

class StringTransformTests extends FreeSpec with Matchers {
  "StringTransforms" - {
    "can camelCase strings" in {
      val toCamel = StringTransforms.camelFunc(false)

      toCamel("test string") shouldEqual "testString"
      toCamel("with three words") shouldEqual "withThreeWords"
      toCamel("with numbers00 mix3d") shouldEqual "withNumbers00Mix3d"
      toCamel("simple") shouldEqual "simple"
      toCamel("") shouldEqual ""
      toCamel("spec!al ch@rs") shouldEqual "spec!alCh@rs"
    }

    "can upper CamelCase strings" in {
      val toCamel = StringTransforms.camelFunc(false)

      toCamel("test string") shouldEqual "testString"
      toCamel("with three words") shouldEqual "withThreeWords"
      toCamel("with numbers00 mix3d") shouldEqual "withNumbers00Mix3d"
      toCamel("simple") shouldEqual "simple"
      toCamel("") shouldEqual ""
      toCamel("spec!al ch@rs") shouldEqual "spec!alCh@rs"
    }

    "can start case strings" in {
      val startCase = StringTransforms.startCaseFunc

      startCase("test string") shouldEqual "Test String"
      startCase("with three words") shouldEqual "With Three Words"
      startCase("with numbers00 mix3d") shouldEqual "With Numbers00 Mix3d"
      startCase("simple") shouldEqual "Simple"
      startCase("") shouldEqual ""
      startCase("spec!al ch@rs") shouldEqual "Spec!al Ch@rs"
    }

    "can snake case strings" in {
      val snakeCase = StringTransforms.underscoreFunc

      snakeCase("test string") shouldEqual "test_string"
      snakeCase("with three words") shouldEqual "with_three_words"
      snakeCase("with numbers00 mix3d") shouldEqual "with_numbers00_mix3d"
      snakeCase("simple") shouldEqual "simple"
      snakeCase("") shouldEqual ""
      snakeCase("spec!al ch@rs") shouldEqual "spec!al_ch@rs"
    }

    "can hyphenate strings" in {
      val hyphenate = StringTransforms.hyphenateFunc

      hyphenate("test string") shouldEqual "test-string"
      hyphenate("with three words") shouldEqual "with-three-words"
      hyphenate("with numbers00 mix3d") shouldEqual "with-numbers00-mix3d"
      hyphenate("simple") shouldEqual "simple"
      hyphenate("") shouldEqual ""
      hyphenate("spec!al ch@rs") shouldEqual "spec!al-ch@rs"
    }

    "can apply the package transformation" in {
      val packaged = StringTransforms.packagedFunc

      packaged("com.chrisbenincasa") shouldEqual "com/chrisbenincasa"
      packaged("com.chrisbenincasa.tools") shouldEqual "com/chrisbenincasa/tools"
      packaged("com.chrisbenincasa/tools") shouldEqual "com/chrisbenincasa/tools"
      packaged("simple") shouldEqual "simple"
      packaged("") shouldEqual ""
      packaged("spec!al.ch@rs") shouldEqual "spec!al/ch@rs"
    }

    "can lowercase strings" in {
      val lowercase = StringTransforms.lowercaseFunc

      lowercase("test string") shouldEqual "test string"
      lowercase("TEST STRING") shouldEqual "test string"
      lowercase("tEsT sTrInG") shouldEqual "test string"
      lowercase("speC!al Ch@rs") shouldEqual "spec!al ch@rs"
      lowercase("") shouldEqual ""
    }

    "can uppercase strings" in {
      val uppercase = StringTransforms.uppercaseFunc

      uppercase("test string") shouldEqual "TEST STRING"
      uppercase("TEST STRING") shouldEqual "TEST STRING"
      uppercase("tEsT sTrInG") shouldEqual "TEST STRING"
      uppercase("speC!al Ch@rs") shouldEqual "SPEC!AL CH@RS"
      uppercase("") shouldEqual ""
    }

    "can capitalize strings" in {
      val capitalize = StringTransforms.capitalizeFunc

      capitalize("test string") shouldEqual "Test string"
      capitalize("with three words") shouldEqual "With three words"
      capitalize("with numbers00 mix3d") shouldEqual "With numbers00 mix3d"
      capitalize("simple") shouldEqual "Simple"
      capitalize("") shouldEqual ""
      capitalize("spec!al ch@rs") shouldEqual "Spec!al ch@rs"
    }

    "can uncapitalize strings" in {
      val decapitalize = StringTransforms.decapitalizeFunc

      decapitalize("test string") shouldEqual "test string"
      decapitalize("Test string") shouldEqual "test string"
      decapitalize("With three words") shouldEqual "with three words"
      decapitalize("") shouldEqual ""
    }

    "can remove non-word characters" in {
      val capitalize = StringTransforms.wordOnlyFunc

      capitalize("test string") shouldEqual "teststring"
      capitalize("with three words") shouldEqual "withthreewords"
      capitalize("with numbers00 mix3d") shouldEqual "withnumbers00mix3d"
      capitalize("simple") shouldEqual "simple"
      capitalize("") shouldEqual ""
      capitalize("spec!al ch@rs") shouldEqual "specalchrs"
    }
  }
}

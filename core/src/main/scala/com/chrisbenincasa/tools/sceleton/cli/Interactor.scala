package com.chrisbenincasa.tools.sceleton.cli

import com.chrisbenincasa.tools.sceleton.filesystem.LoadProperties.Props
import scala.util.Try

object Interactor {
  def apply(props: Props, prefilledProps: Props): Try[Props] = {
    Try {
      val (desc, propsToGet) = props.partition { case (k, _) => k.equalsIgnoreCase("description") }

      desc.foreach {
        case (_, v) =>
          println(v)
          println()
      }

      propsToGet.filterKeys(!prefilledProps.isDefinedAt(_)).foldLeft(Props.empty) {
        case (acc, (k, v)) =>
          print(s"$k [$v]: ")
          Console.flush()
          val input = scala.io.StdIn.readLine().trim
          val v2 = if (input.isEmpty) v else input
          acc + (k -> v2)
      }
    }.map(_ ++ prefilledProps)
  }
}

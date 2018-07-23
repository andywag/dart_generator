package org.simplifide.dart.web

import org.simplifide.template.Container
import org.simplifide.template.FileModel.GFile
import org.simplifide.template.model.{MClassFile, Model}
import org.simplifide.template.model.dart.{DartGenerator, DartParser}
import org.simplifide.utils.Utils

trait DartFile extends Container[Model] with DartParser {
  val filename:String

  implicit val creator = DartGenerator.create _
  def createFile = GFile(filename,this.contents)
}

object DartFile {
  trait DartClassFile extends DartFile with MClassFile {
    val filename = Utils.camelToUnderscores(className) + ".dart"

  }
}

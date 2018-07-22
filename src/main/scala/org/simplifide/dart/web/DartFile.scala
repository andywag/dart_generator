package org.simplifide.dart.web

import org.simplifide.template.Container
import org.simplifide.template.FileModel.GFile
import org.simplifide.template.model.Model
import org.simplifide.template.model.dart.{DartGenerator, DartParser}

trait DartFile extends Container[Model] with DartParser{
  val filename:String

  implicit val creator = DartGenerator.create _
  def createFile = GFile(filename,this.contents)

}

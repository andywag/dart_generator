package org.simplifide.dart.binding

import org.simplifide.template.Container
import org.simplifide.template.FileModel.GFile
import org.simplifide.template.model.Model
import org.simplifide.template.model.dart.{DartGenerator, DartParser}

trait DataModel {
  val cla:MClassProto

  def create = {
    new Container[Model] with DartParser {
      cla.imports.foreach(x => -->(x))
      -->(org.simplifide.template.model.Model.Line)
      -->(cla.create)

    }
  }

  def createFile = {
    val gen = create
    val className = cla.name.toLowerCase()
    new GFile(className + ".dart",gen.contents(DartGenerator.create))
  }

}

object DataModel {
  case class DataModelImpl(cla:MClassProto) extends DataModel
}

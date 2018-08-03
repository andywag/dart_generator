package org.simplifide.dart.web

import org.simplifide.dart.core.Importable
import org.simplifide.template.Container
import org.simplifide.template.FileModel.GFile
import org.simplifide.template.model.{MClassFile, Model}
import org.simplifide.template.model.dart.{DartGenerator, DartParser}
import org.simplifide.utils.Utils

trait DartFile extends Container[Model] with DartParser {
  val filename:String
  implicit val path:Option[String] = None

  val imports:List[Model.Import] = List()


  implicit val creator = DartGenerator.create _
  def createFile = {
    val content = this.contents(imports)
    GFile(filename,content)
  }
}

object DartFile {
  trait DartClassFile extends DartFile with MClassFile {
    lazy val filename = Utils.camelToUnderscores(className) + ".dart"

  }
}

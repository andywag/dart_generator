package org.simplifide.dart.binding

import org.simplifide.dart.binding.MockModel.MockClass
import org.simplifide.dart.web.DartFile
import org.simplifide.dart.web.DartFile.DartClassFile
import org.simplifide.template.model.Model.MClass
import org.simplifide.template.model.Model._
import org.simplifide.utils.Utils



case class MockModel(service:ModelService) extends DartClassFile {

  val className = service.mockName
  val mClass = MockClass(this.service)

  IMPORT_DART_ASYNC
  IMPORT_DART_CONVERT
  IMPORT_DART_MATH
  IMPORT_HTTP
  IMPORT_PACKAGE_TESTING
  imp(service.importName)

  -->(Line)
  -->(mClass)
}

object MockModel {
  case class MockClass(service:ModelService) extends MClass(service.mockName) {
    override val parents = List(new MClass("MockClient"))
  }

}

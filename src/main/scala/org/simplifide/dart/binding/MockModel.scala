package org.simplifide.dart.binding

import org.simplifide.dart.binding.MockModel.MockClass
import org.simplifide.dart.web.{DartFile, DartWebProject}
import org.simplifide.dart.web.DartFile.DartClassFile
import org.simplifide.template.model.Model.MClass
import org.simplifide.template.model.Model._
import org.simplifide.utils.Utils



case class MockModel(service:ModelService) extends DartClassFile {

  lazy val mClass = MockClass(this.service)
  override val classPath = DartWebProject.TEST_PATH

  IMPORT_DART_ASYNC
  IMPORT_DART_CONVERT
  IMPORT_DART_MATH
  IMPORT_HTTP
  IMPORT_PACKAGE_TESTING

  importClass(this.service.classFile, Some(this))


  -->(Line)
  -->(mClass)
}

object MockModel {
  case class MockClass(service:ModelService) extends MClass(service.mockName) {
    override val parents = List(new MClass("MockClient"))
  }

}

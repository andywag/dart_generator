package org.simplifide.dart.binding

import org.simplifide.dart.binding.MockModel.MockClass
import org.simplifide.dart.web.DartFile
import org.simplifide.template.model.Model.MClass
import org.simplifide.template.model.Model._



case class MockModel( service:ModelService) extends DartFile {

  val filename = s"in_memory_${service.serviceName.toLowerCase}.dart"

  IMPORT_DART_ASYNC
  IMPORT_DART_CONVERT
  IMPORT_DART_MATH
  IMPORT_HTTP
  IMPORT_PACKAGE_TESTING
  imp(service.importName)
  -->(Line)
  -->(MockClass(service))
}

object MockModel {
  case class MockClass(service:ModelService) extends MClass(s"mock_${service.serviceName}") {
    override val parents = List(new MClass("MockClient"))
  }

}

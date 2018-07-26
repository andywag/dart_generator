package org.simplifide.dart.core

import org.simplifide.template.model.MAttribute.MAttr
import org.simplifide.template.model.Model
import org.simplifide.template.model.Model.Import
import org.simplifide.template.model.dart.DartModel.{DartBuiltIn, DartImport, DartPackage}

object DartPackages {

  def imp(x:Model) = Model.Import(x)

  val ANGULAR        = DartPackage("angular","angular.dart")
  val ANGULAR_ROUTER = DartPackage("angular_router","angular_router.dart")
  val ASYNC          = DartBuiltIn("async")
  val CONVERT        = DartBuiltIn("convert")
  val MATH           = DartBuiltIn("math")
  val HTTP = DartPackage("http","http.dart")
  val HTTP_TESTING= DartPackage("http","testing.dart")



  object Injectable extends MAttr("Injectable")

}

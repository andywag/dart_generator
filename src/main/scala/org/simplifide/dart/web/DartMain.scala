package org.simplifide.dart.web

import org.simplifide.template.model.MFunction.MFunc
import org.simplifide.template.model.MType.{$final, SType}
import org.simplifide.template.model.Model._
import org.simplifide.template.model.dart.DartParser

case class DartMain(name:String) extends DartFile {

  val filename = "main.dart"

  IMPORT_ANGULAR
  IMPORT_ANGULAR_ROUTER
  import_pack(name,"app_component.template.dart",Some("ng"))

  IMPORT_SELF

  -->(Line)
  --@("GenerateInjector")(
    "routerProvidersHash"
  )
  -->(Line)
  val inj = dec($final ~ T("InjectorFactory") ~ "injector" ~= "self.injector$Injector")
  -->(Line)
  object Func extends MFunc("main",SType("void")) with DartParser {
    val args = List()

    call("runApp",List("ng.AppComponentNgFactory","createInjector"~>inj),true)
  }
  -->(Func)

}

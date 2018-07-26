package org.simplifide.dart.web

import org.simplifide.dart.core.Importable
import org.simplifide.template.model.{MClassFile, MFunction, Model}
import org.simplifide.template.model.MFunction.MFunc
import org.simplifide.template.model.MType.{$final, SType}
import org.simplifide.template.model.Model._
import org.simplifide.template.model.dart.DartParser

case class DartMain(name:String, client:Option[MClassFile] = None) extends DartFile {

  import org.simplifide.dart.core.DartPackages._
  import org.simplifide.dart.core.Importable._

  val filename = "main.dart"
  override implicit val path = None

  override val imports = List(i(ANGULAR),i(ANGULAR_ROUTER),i(HTTP))


  import_pack(name,"app_component.template.dart",Some("ng"))
  client.map(x => -->(x.importPackage(name)))

  IMPORT_SELF

  client.map(x => MFunction.Call("ClassProvider",List("Client",ModelTuple("useClass",x.className)))).getOrElse(Model.Empty)
  //ClassProvider(Client, useClass: InMemoryDataService),

  val res = client.map(x => MFunction.Call("ClassProvider",List("Client",ModelTuple("useClass",x.className)))).getOrElse(Model.Empty)


  val injectItems:List[Model] = List("routerProvidersHash",
    client.map(x => MFunction.Call("ClassProvider",List("Client",ModelTuple("useClass",x.className)))).getOrElse(Model.Empty))



  -->(Line)
  --@("GenerateInjector")(
    Model.MArray(injectItems)
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

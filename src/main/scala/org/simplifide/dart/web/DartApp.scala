package org.simplifide.dart.web

import org.simplifide.dart.binding.ModelService
import org.simplifide.dart.web.Routes.RoutePath
import org.simplifide.template.model.MClass.MClassBase
import org.simplifide.template.model.MType.{$final, SType, TString}
import org.simplifide.template.model.MVar.Var
import org.simplifide.template.model.{MClass, MType, Model}
import org.simplifide.template.model.Model.Quotes
import org.simplifide.template.model.css.CssModel.CssFile
import org.simplifide.template.model.dart.DartClass.DartClassBase
import org.simplifide.template.model.dart.{DartClass, DartParser}
import scalatags.Text.all._

case class DartApp(routes:List[RoutePath], override val services:List[ModelService]) extends DartComponent {

  def createRoute(route:RoutePath) = {
    a(attr("[routerLink]",raw=true):=s"routes.${route.name}.toUrl()",attr("routerLinkActive"):="active")(s"${route.name}")
  }

  val name     = "app"
  val template = div(
    h1("{{title}}"),
    tag("nav")(
      routes.map(x => createRoute(x))
    ),
    tag("router-outlet")(attr("[routes]",raw=true):="routes.all")
  )
  override val style = Some(CssFile(DartWebStyles.MyInline))
  val directives:List[Model] = List("routerDirectives")
  val providers:List[Model] = "ClassProvider(Routes)" :: services.map(x => Model.Str(s"ClassProvider(${x.serviceName})"))

  // FIXME : Should not have hardcoded imports
  override val imports:List[Model] = List(Model.Import("../routes.dart"),Model.Import("../services/event_service.dart"))
    //services.map(x => x.classFile.importCommand)

  override def createClass:MClass = DartApp.DartAppClassBase("DartApp",SType("Routes"))

}

object DartApp {
  case class DartAppClassBase(title:String, routeType:MType) extends DartClassBase("AppComponent")  {//extends MClassBase("AppComponent") with DartParser {
    override val members = List(
      Var("title",$final ~ TString,Some(Quotes(title))),
      routeType.cVar($final)
    )
  }
}

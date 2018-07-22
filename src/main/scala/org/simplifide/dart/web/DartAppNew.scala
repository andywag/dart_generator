package org.simplifide.dart.web

import org.simplifide.dart.web.Routes.RoutePath
import org.simplifide.template.model.MType.$final
import org.simplifide.template.model.Model
import org.simplifide.template.model.Model.MClass
import org.simplifide.template.model.css.CssModel.CssFile
import org.simplifide.template.model.dart.DartParser
import scalatags.Text.all._

case class DartAppNew(routes:List[RoutePath]) extends DartComponent {

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
  val providers:List[Model] = List("ClassProvider(Routes)")

  override val imports:List[Model] = List(Model.Import("src/routes.dart"))

  override def createClass:MClass = DartAppNew.DartAppClass("DartApp",routes)

}

object DartAppNew {
  case class DartAppClass(title:String, routes:List[RoutePath]) extends MClass("AppComponent") with DartParser {
    dec($final ~ T("String") ~ "title" ~= Model.Quotes(title) )
    dec($final ~ T("Routes") ~ "routes")
    call("AppComponent",List("this.routes"), true)
  }
}

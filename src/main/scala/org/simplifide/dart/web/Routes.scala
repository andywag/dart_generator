package org.simplifide.dart.web

import org.simplifide.dart.core.{DartPackages, DartTypes}
import org.simplifide.dart.core.DartPackages.Injectable
import org.simplifide.template.model.MAttribute.MAttr
import org.simplifide.template.model.MClass.MClassBase
import org.simplifide.template.model.MFunction.SFunction
import org.simplifide.template.model.MType.$final
import org.simplifide.template.model.MVar.{Var, VarDec}
import org.simplifide.template.model.{MFunction, MType, Model}
import org.simplifide.template.model.Model.$import
import org.simplifide.template.model.dart.DartParser
import org.simplifide.template.model.Model._
import org.simplifide.template.model.dart.DartClass.DartClassBase

object Routes {

  case class RoutePath(name:String, path:DartComponent) {
    val pName = s"p_$name"
  }

  case class RouteFile(paths:List[RoutePath], routePathFile:DartFile) extends DartFile {
    val filename = "routes.dart"
    override implicit val path:Option[String] = Some("src")
    import org.simplifide.dart.core.Importable._

    val componentImports = paths.map(x => i(x.path.file) as x.pName)

    override val imports = List(
      i(DartPackages.ANGULAR),
      i(DartPackages.ANGULAR_ROUTER),
      i(routePathFile) as "paths") ::: componentImports


    object RouteClassBase$ extends MClassBase("Routes") with DartParser {

      def pathName(path:RoutePath) = s"paths.${path.name}"
      def createDec(path:RoutePath) =
        lambda("get",T("RoutePath"),path.name,pathName(path))

      def getRouteList() = {
        def createRoute(path:RoutePath) = {
          val finalComp = s"${path.pName}.${path.path.className}NgFactory"
          val result = DartTypes.RouteDefinition(routePath = pathName(path),component=finalComp)
          result
        }
        paths.map(x => createRoute(x))
      }
      paths.foreach(x => createDec(x))
      blankLine
      -->($final ~ MType.TList(DartTypes.RouteDefinition) ~ "all" ~= Model.FixListLine(getRouteList()))

    }


    blankLine
    -->(Injectable)
    -->(RouteClassBase$)
  }



}

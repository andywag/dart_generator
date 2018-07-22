package org.simplifide.dart.web

import org.simplifide.template.model.MAttribute.MAttr
import org.simplifide.template.model.MFunction.SFunction
import org.simplifide.template.model.MType.$final
import org.simplifide.template.model.MVar.{Var, VarDec}
import org.simplifide.template.model.{MFunction, Model}
import org.simplifide.template.model.Model.{$import, MClass}
import org.simplifide.template.model.dart.DartParser
import org.simplifide.template.model.Model._

object Routes {

  case class RoutePath(name:String, path:DartComponent)
  // FIXME : Create a real path rather than a string
  //case class RoutePathA(name:Arg[String]=Empty[String], path:Arg[DartComponent]=Empty[DartComponent])

  case class RouteDefinition(routePath:Model,
                             component:Model) extends SFunction



  object Injectable extends MAttr("Injectable")


  case class RouteFile(paths:List[RoutePath]) extends DartFile {
    val filename = "routes.dart"

    def importName(path:RoutePath) = s"p_${path.name}"

    def createImports(path:RoutePath) = {
      -->($import ~ s"${path.path.componentName}.template.dart" as importName(path))
    }





    object RouteClass extends MClass("Routes") with DartParser {
      def createDec(path:RoutePath) = {
        lambda("get",T("RoutePath"),path.name,s"paths.${path.name}")
      }


      def getRouteList() = {
        def createRoute(path:RoutePath) = {
          val finalComp = s"${importName(path)}.${path.path.className}NgFactory"
          MFunction.generic(RouteDefinition(s"paths.${path.name}",finalComp))
        }
        paths.map(x => createRoute(x))
      }

      paths.foreach(x => createDec(x))
      blankLine
      -->($final ~ T("List<RouteDefinition>") ~ "all" ~= Model.FixListLine(getRouteList()))
    }


    IMPORT_ANGULAR
    IMPORT_ANGULAR_ROUTER
    val path = -->($import ~ "route_paths.dart" as "paths")
    blankLine
    paths.foreach(x => createImports(x))
    blankLine
    -->(Injectable)
    -->(RouteClass)
  }

  case class RoutePathsFile(paths:List[RoutePath]) extends DartFile {

    val filename = "route_paths.dart"

    def createPath(path:RoutePath) = {
      val v = Var(path.name,$final ~ T("RoutePath"))
      val de = VarDec(v).copy(value = Some(fun("RoutePath",List("path"~>Model.Quotes(path.path.selector)))))
      dec(de)
    }

    IMPORT_ANGULAR_ROUTER
    -->(Model.Line)
    paths.map(x => createPath(x))
  }

}

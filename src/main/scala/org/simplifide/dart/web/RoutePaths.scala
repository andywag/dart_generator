package org.simplifide.dart.web

import org.simplifide.dart.core.{DartPackages, DartTypes}
import org.simplifide.dart.web.Routes.RoutePath
import org.simplifide.template.model.MType.$final
import org.simplifide.template.model.Model

class RoutePaths {

}

object RoutePaths {
  case class RoutePathsFile(paths:List[RoutePath]) extends DartFile {
    val filename = "route_paths.dart"
    override implicit val path:Option[String] = Some("src")

    import org.simplifide.dart.core.Importable._
    override val imports = List(i(DartPackages.ANGULAR_ROUTER))

    def createPath(path:RoutePath) = {
      -->($final ~ DartTypes.routePath ~ path.name ~= DartTypes.RoutePath(path=Model.Quotes(path.path.selector)))
    }

    -->(Model.Line)
    paths.map(x => createPath(x))
  }
}

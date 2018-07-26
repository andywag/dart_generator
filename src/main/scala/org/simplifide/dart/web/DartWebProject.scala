package org.simplifide.dart.web

import org.simplifide.dart.binding.{DataModel, MockModel, ModelService}
import org.simplifide.dart.web.RoutePaths.RoutePathsFile
import org.simplifide.dart.web.Routes.RoutePath
import org.simplifide.template.FileModel
import org.simplifide.template.FileModel.{GCopy, GDir, GList, GResource}
import org.simplifide.template.model.dart.DartProject.Dependency
import org.simplifide.template.model.dart.PubSpec

trait DartWebProject  {
  val name:String
  val description:String

  val components:List[DartComponent]

  val sources:List[FileModel]
  val dependencies:List[Dependency]
  val devDependencies:List[Dependency]
  val routes:List[RoutePath]

  val models:Map[String,DataModel]
  val services:List[ModelService]
  val mockItems:List[MockModel]



  private lazy val componentSources = components.flatMap(x => x.createFiles)



  protected lazy val main = DartMain(name)
  private lazy val routePathsFile = RoutePathsFile(routes)
  private lazy val routeFile     = Routes.RouteFile(routes, routePathsFile)


  def create = {

    GDir(name) (
      GDir("lib") (
        GDir("src",
          GDir("models",models.map(x=>x._2).map(x => x.createFile).toList) ::
          GDir("services", services.map(x => x.file)) ::
          GDir("test", mockItems.map(x => x.createFile)) ::
            GDir("components",componentSources) ::
          routeFile.createFile ::
          routePathsFile.createFile ::
          //componentSources :::
          sources ),
        GList(DartApp(routes, services).createFiles)
      ),
      GDir("test"),
      GDir("web",List(
        main.createFile,
        //GCopy("favicon.png","C:\\dart_projects\\toh2\\web\\favicon.png"),
        //GCopy("index.html","C:\\dart_projects\\toh2\\web\\index.html"),
        //GCopy("styles.css","C:\\dart_projects\\toh2\\web\\styles.css"),
        GResource("favicon.png","web/favicon.png"),
        GResource("index.html" ,"web/index.html"),
        GResource("styles.css" ,"web/styles.css")
      )),
      PubSpec(PubSpec.Description(this.name,""),dependencies,devDependencies).create
    )
  }
}

object DartWebProject {

  val MODEL_PATH   = "src/models/"
  val SERVICE_PATH = "src/services/"
  val TEST_PATH     = "src/test/"
  val COMPONENT_PATH     = "src/components/"


  val defaultDependencies = List(
    Dependency("angular", "^5.0.0-alpha+15"),
    Dependency("angular_forms", "^2.0.0-alpha"),
    Dependency("angular_router", "^2.0.0-alpha"),
    Dependency("http", "^0.11.0"),
    Dependency("stream_transform", "^0.0.6")
  )
  val defaultDevDependencies = List(
    Dependency("angular_test",        "^2.0.0-alpha"),
    Dependency("build_runner",        "^0.8.10"),
    Dependency("build_test",          "^0.10.2"),
    Dependency("build_web_compilers", "^0.4.0"),
    Dependency("mockito",             "^3.0.0-beta+1"),
    Dependency("pageloader",          "^3.0.0-alpha"),
    Dependency("test",                "^1.0.0")
  )
}
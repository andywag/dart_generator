package template

import java.io.File
import java.nio.file.Files

import org.scalatest.FlatSpec
import org.simplifide.dart.binding.{Binding, ModelService}
import org.simplifide.dart.binding.Binding._
import org.simplifide.dart.binding.DataModel.DataModelImpl
import org.simplifide.dart.binding.ModelService.ModelServiceI
import org.simplifide.dart.web.Routes.RoutePath
import org.simplifide.dart.web.{DartCommands, DartComponent, DartWebProject, DartWebStyles}
import org.simplifide.template.FileModel
import org.simplifide.template.model.Model
import org.simplifide.template.model.css.CssModel.CssFile
import org.simplifide.template.model.dart.{DartConstants, DartProject}
import org.simplifide.template.model.dart.DartProject.Dependency
import org.simplifide.template.model.html.HtmlModel
import org.simplifide.utils.Utils
import scalatags.Text.all._


object DartProjectExample extends DartWebProject {
  val name        = "dart_example"
  val description = "basic dart starting point example"
  val sources = List()
  val dependencies = DartWebProject.defaultDependencies
  val devDependencies = DartWebProject.defaultDevDependencies

  val components = List(TestComponent1, TestComponent2, TestComponent3)
  val routes     = List(RoutePath("test1",TestComponent1),
    RoutePath("test2",TestComponent2),
    RoutePath("test3",TestComponent3),
  )

  val models = ProjectModel.models
  val services = List(ModelService.all(ProjectModel.models.get(ProjectModel.event.id).get.cla,"event"))

}

object ProjectModel {
  import io.circe.generic.auto._

  case class Person(x: String, d: Option[String], id: String = "Person")
  case class Result(x: String, y: Person, id: String = "Result")
  case class Event(name: String, event: String, results: List[Result], id: String = "Event")

  val person = Person(B_STRING,Some(B_STRING))
  val result = Result(B_STRING,person)
  val event = Event(B_STRING,B_STRING,List(result))

  //val models = Binding.createClassMap(event).map(x => (x,DataModelImpl(x))
  val models = Binding.createClassMap(event).map(x => (x._1,DataModelImpl(x._2))).toMap

}
object TestComponent1 extends DartComponent {
  val name     = "test_a"
  override val style = Some(CssFile(DartWebStyles.MyInline))
  val template:HtmlModel = h1(p("Here I am 1"))
  val directives:List[Model] = List(DartConstants.ROUTER_DIRECTIVE)
  val providers:List[Model] = List()
}

object TestComponent2 extends DartComponent {
  val name     = "test_b"

  val template:HtmlModel = h1(p("Here I am 12"))
  val directives:List[Model] = List(DartConstants.ROUTER_DIRECTIVE)
  val providers:List[Model] = List()
}

object TestComponent3 extends DartComponent {
  val name     = "test_c"

  val template:HtmlModel = h1(p("Here I am 13"))
  val directives:List[Model] = List(DartConstants.ROUTER_DIRECTIVE)
  val providers:List[Model] = List()
}


class DartProjectTest extends FlatSpec{
  val base =  s"C:\\dart_projects\\generated\\${DartProjectExample.name}"

  FileModel.create(DartProjectExample.create,new File(base).getParentFile)

  DartCommands.setup(s"$base")
  DartCommands.build(base)
  DartCommands.run(22222,s"$base")

  assert (true === true)

}

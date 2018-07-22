package org.simplifide.dart.web

import org.simplifide.dart.web.DartComponent.DartComponentFile
import org.simplifide.template.Container
import org.simplifide.template.FileModel.GFile
import org.simplifide.template.model.MAttribute.MAttr
import org.simplifide.template.model.Model
import org.simplifide.template.model.Model.MClass
import org.simplifide.template.model.css.CssModel
import org.simplifide.template.model.dart.{DartModel, DartParser}
import org.simplifide.template.model.html.HtmlModel
import org.simplifide.template.model.Model._

/**
  * Trait which contains a dart component along with the file that contains it
  */
trait DartComponent extends Model{

  val name:String
  val template:HtmlModel
  val style:Option[CssModel] = None
  val directives:List[Model]
  val providers:List[Model]

  val imports:List[Model] = List()

  lazy final val selector:String = s"my-$name"
  lazy final val componentName:String = s"${name}_component"
  lazy final val className:String = componentName.split("_").map(_.capitalize).mkString("")

  def createClass:MClass = DartComponent.EmptyComponentClass(this)

  def create = new DartComponent.Component(this)

  lazy val cssFileName = s"$componentName.css"
  lazy val cssFile = style.flatMap(x => CssModel.createFile(x,cssFileName))

  def createFiles = {


    val contents = DartComponentFile(this).contents(DartModel.dartWriter)
    val dart = GFile(s"$componentName.dart",contents)

    cssFile.map(x => List(x,dart)).getOrElse(List(dart))

  }

}

object DartComponent {

  case class EmptyComponentClass(component: DartComponent) extends MClass(component.className){}

  class Component(component:DartComponent) extends MAttr("Component") {
    -->('selector ~> Model.Quotes(component.selector))
    -->('template ~> Model.Quotes3(component.template))
    -->('directives ~> Model.FixList(component.directives))
    -->('providers ~> Model.FixList(component.providers))
    component.style.map(x => -->(CssModel.createStyle(x, component.cssFileName)))
  }

  case class DartComponentFile(component:DartComponent) extends Container[Model] with DartParser {
    IMPORT_ANGULAR
    IMPORT_ANGULAR_ROUTER
    component.imports.foreach(-->(_))

    -->(Line)
    -->(new Component(component))
    -->(Line)
    -->(component.createClass)
  }

}



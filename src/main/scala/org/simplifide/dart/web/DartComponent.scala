package org.simplifide.dart.web

import org.simplifide.dart.binding.ModelService
import org.simplifide.dart.web.DartComponent.DartComponentFile
import org.simplifide.template.Container
import org.simplifide.template.FileModel.GFile
import org.simplifide.template.model.MAttribute.MAttr
import org.simplifide.template.model.MClass.MClassBase
import org.simplifide.template.model.MType.{$final, SType}
import org.simplifide.template.model.{MClass, MClassFile, Model}
import org.simplifide.template.model.css.CssModel
import org.simplifide.template.model.dart.{DartModel, DartParser}
import org.simplifide.template.model.html.HtmlModel
import org.simplifide.template.model.Model._
import org.simplifide.utils.Utils

/**
  * Trait which contains a dart component along with the file that contains it
  */
trait DartComponent extends Model{

  val name:String
  val template:HtmlModel
  val style:Option[CssModel] = None
  val directives:List[Model]
  val providers:List[Model]
  val services:List[ModelService] = List()

  val imports:List[Model] = List()

  lazy final val file = DartComponentFile(this)
  lazy final val selector:String = s"my-$name"
  lazy final val componentName:String = s"${name}_component"
  lazy final val className:String = componentName.split("_").map(_.capitalize).mkString("")


  def createClass:MClass = DartComponent.EmptyComponentClassBase(this)

  def create = new DartComponent.Component(this)

  lazy val cssFileName = s"$componentName.css"
  lazy val cssFile = style.flatMap(x => CssModel.createFile(x,cssFileName))

  def createFiles = {


    val contents = file.contents()(DartModel.dartWriter)
    val dart = GFile(s"$componentName.dart",contents)

    cssFile.map(x => List(x,dart)).getOrElse(List(dart))

  }

}

object DartComponent {

  case class EmptyComponentClassBase(component: DartComponent) extends MClassBase(component.className) {
    component.services.map(x => -->($final ~ SType(x.serviceName) ~ Utils.camelToUnderscores(x.serviceName) ))
  }

  class Component(component:DartComponent) extends MAttr("Component") {
    -->('selector ~> Model.Quotes(component.selector))
    -->('template ~> Model.Quotes3(component.template))
    -->('directives ~> Model.FixList(component.directives))
    -->('providers ~> Model.FixList(component.providers))
    component.style.map(x => -->(CssModel.createStyle(x, component.cssFileName)))
  }

  case class DartComponentFile(component:DartComponent) extends DartFile with MClassFile{

    override val mClass: MClass = component.createClass
    override val filename: String = Utils.camelToUnderscores(mClass.name.string) +".dart"
    override implicit val path:Option[String] = Some(DartWebProject.COMPONENT_PATH)


    IMPORT_ANGULAR
    IMPORT_ANGULAR_ROUTER
    component.imports.foreach(-->(_))
    //component.services.foreach(x => -->(importClass(x.classFile,Some(this))))

    -->(Line)
    -->(new Component(component))
    -->(Line)
    -->(mClass)
  }

}



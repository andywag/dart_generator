package org.simplifide.dart.binding

import org.simplifide.dart.binding.MockModel.MockClassBase
import org.simplifide.dart.web.{DartFile, DartWebProject}
import org.simplifide.dart.web.DartFile.DartClassFile
import org.simplifide.template.model.MClass.MClassBase
import org.simplifide.template.model.MType.{$final, $static, TFuture, TResponse}
import org.simplifide.template.model.{MFunction, MType, MVar, Model}
import org.simplifide.template.model.Model._
import org.simplifide.utils.Utils



case class MockModel(service:ModelService, ret:String) extends DartClassFile {

  lazy val mClass = MockClassBase(this.service, ret)
  override val classPath = DartWebProject.TEST_PATH

  IMPORT_DART_ASYNC
  IMPORT_DART_CONVERT
  IMPORT_DART_MATH
  IMPORT_HTTP
  IMPORT_PACKAGE_TESTING

  importClass(this.service.classFile, Some(this))


  -->(Line)
  -->(mClass)
}

object MockModel {

  case class MockClassBase(service:ModelService, str:String) extends MClassBase(service.mockName) {
    override val parents = List(new MClassBase("MockClient"))

    val data = -->($static ~ $final ~ 'data ~= str)
    -->(Line)
    items.append(new ResponseFunction(service))
    -->(Line)
    items.append(new Construct(service))

  }

  class ResponseFunction(service:ModelService) extends MFunction.MFunc("_handler",TFuture(TResponse)) {
    val args = List(MVar.Var("request",MType.SType("Request")))
    override val prefix = Some(Str("static"))
    override val postfix = Some(Str("async"))



    val dataIn = Model.Dictionary(List(Model.ModelTuple("data","data")))
    val data = MFunction.Call("json.encode",List(dataIn))

    val content = Model.Dictionary(List(Model.ModelTuple("content-type",Model.Quotes("application/json"))))
    val header = Model.ModelTuple("headers",content)

    //val headers = Model.Dictionary(List(ModelTuple("headers",ModelTuple("content-type","application/json"))))
    -->(Model.Return(MFunction.Call("Response",List(data,200,header))))

  }

  class Construct(service:ModelService) extends MFunction.Constructor(service.mockName,List("_handler")) {
    val body = List()
    override val args: List[MVar.Var] = List()
    override val output: MType = MType.NoType
  }

}

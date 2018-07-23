package org.simplifide.dart.binding

import org.simplifide.dart.binding
import org.simplifide.dart.web.DartFile
import org.simplifide.template.FileModel.GFile
import org.simplifide.template.model.MType.{$final, $static, SType, TDynamic}
import org.simplifide.template.model.MVar.Var
import org.simplifide.template.model.{MFunction, Model}
import org.simplifide.template.model.Model.{MClass, SemiEnd}
import org.simplifide.template.model.dart.{DartGenerator, DartParser}

trait ModelService {
  val proto:MClassProto
  val functions:Map[String,ResponseFunction]


  def file = {
    val file = new binding.ModelService.ModelServiceFile(this)
    new GFile(file.filename + "_service.dart",file.contents(DartGenerator.create))
  }

  lazy val serviceName = s"${proto.name}Service"
  lazy val importName  = s"services/${proto.name.toLowerCase}_service.dart"
  lazy val mockName =   s"InMemory" + serviceName

}

object ModelService {
  val extractData:String = "_extractData"

  case class ModelServiceI(proto:MClassProto, functions:Map[String,ResponseFunction]) extends ModelService
  val client = $final ~ SType("Client") ~ "_http"

  def all(proto:MClassProto, address:String) = new ModelServiceI(proto,
    Map(proto.name->ResponseFunction.All(proto.typ,Model.Quotes(address),proto.fromFunction,client.v)))

  class ModelServiceFile(service:ModelService) extends DartFile {
    val proto = service.proto
    override val filename: String = proto.name.toLowerCase

    IMPORT_DART_ASYNC
    IMPORT_DART_CONVERT
    IMPORT_HTTP
    IMPORT_ANGULAR
    imp(s"../models/${proto.name.toLowerCase}.dart")
    -->(Model.Line)
    $_INJECTABLE
    -->(ModelServiceClass(service))


  }

  case class ModelServiceClass(service:ModelService) extends MClass(s"${service.serviceName}") {
    -->($static ~ $final ~ "_headers" ~= Model.Dictionary(List(Model.ModelTuple("Content-Type",Model.Quotes("application/json")))))
    //-->($static ~ $final ~ "_url" ~= Model.Quotes(service.baseAddress))
    -->(Model.Line)
    val client = -->($final ~ SType("Client") ~ "_http")
    -->(Model.Line)
    -->(new SemiEnd(DartParser.constructor(this.name.string,List(client.v))))
    -->(Model.Line)
    this.items.append(ExtractData)
    -->(Model.Line)
    service.functions.map(x => -->(x._2))


  }

  //   dynamic _extractData(Response resp) => json.decode(resp.body)['data'];
  object ExtractData extends MFunction.MFunc(extractData,TDynamic) {
    val resp = Var("resp",SType("Response"))
    val args = List(resp)

    -->(Model.Return(MFunction.Call(s"json.decode",List(resp.d("body"))).s("data")))
  }





}

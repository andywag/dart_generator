package org.simplifide.dart.binding

import org.simplifide.template.model.MFunction.AnonLambda
import org.simplifide.template.model.MTryCatch.{Catch, Try}
import org.simplifide.template.model.MType.{$auto, $final}
import org.simplifide.template.model.MVar.Var
import org.simplifide.template.model.Model.Wait
import org.simplifide.template.model.{MFunction, MType, MVar, Model}



trait ResponseFunction extends MFunction {
  val name: Model
  val out: MType
  val client: MVar
  val address: Model
  val converter: (MVar) => List[Model]


  lazy val output: MType = MType.TFuture(out)
  override val postFix: Option[Model] = Some("async")

  val args: List[Var] = List()

  def body: List[Model] = List(TryBody, CatchBody)

  object TryBody extends Try {
    val response = -->($final ~ 'response ~= Wait(client.f("get", address)))
    val data = -->($auto ~ 'data ~= MFunction.Call(ModelService.extractData, List(response.v)))
    converter(data.v).map(x => -->(x))
  }

  object CatchBody extends Catch("e") {

  }
}



object ResponseFunction {
  case class All(o:MType,
                 address:Model,
                 fromJson:MFunction,
                 client:MVar) extends ResponseFunction {

    val name:Model = "getAll"
    lazy val out:MType = MType.TList(o) // Convert the output to a list

    val converter:(MVar)=>List[Model] = {case x => {
      val conv = $auto ~ 'conv ~= Model.As(x,MType.TSList)

      List(
        conv,
        Model.Return(conv.v.f("map",AnonLambda(conv.v,fromJson(List(conv.v)))).f("toList"))
      )
    }}
  }

}


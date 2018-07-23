package org.simplifide.dart.binding

import org.simplifide.dart.binding.MClassProto.{From, To}
import org.simplifide.template.model.MFunction.Factory
import org.simplifide.template.model.MType._
import org.simplifide.template.model.MVar.VarDec
import org.simplifide.template.model.Model.{DotPrefix, MClass}
import org.simplifide.template.model.{MFunction, MType, MVar, Model}
import org.simplifide.template.model.Model._

trait MClassProto extends Model {
  val name:String
  val vars:List[MVar.Var]
  val fields:List[Model]

  // FIXME : This isn't correct
  def typ = SType(name)

  val fromFunction =  new From(this)
  val toFunction   =  new To(this)

  def declarations = {
    val a = types
    vars.map(x => VarDec(x,None))
  }

  // FIXME : Should generalize imports rather than the assumption it is a lower case import of the class name
  def imports = {
    def typeName(typ:MType):Option[String] = {
      typ match {
        case SType(x) => Some(x.toLowerCase)
        case _ => None
      }
    }
    types.flatMap(typeName(_)).map(x => Model.Import(s"${x}.dart") )
  }


  def types = {
    def decode(typ:MType):List[MType] = {
      typ match {
        case x: BaseTypes => {
          List()
        }
        case SType(x) => List(typ)
        case x: Generic => decode(x.o) ::: x.i.flatMap(y => decode(y))
        case TypeAnd(r, l) => decode(r) ::: decode(l)
      }
    }
    val typs = vars.flatMap(x => decode(x.typ))
    typs
  }


  def constructor = {
    new MFunction.Call(this.name,this.vars.map(x => x.copy(name = DotPrefix("this",x.name))))
  }

  def create = new MClassProto.Cla(this)

}

object MClassProto {

  case class MClassProtoImpl(name:String,
                             vars:List[MVar.Var],
                             fields:List[Model]) extends MClassProto

  class Cla(cla:MClassProto) extends MClass(cla.name) {
    cla.declarations.foreach(x => -->(x))
    -->(Model.Line)
    -->(new SemiEnd(cla.constructor))
    -->(Line)
    this.items.append(new From(cla))
    -->(Line)
    this.items.append(new To(cla))

  }


  class From(cla:MClassProto) extends MFunction.MFunc(s"${cla.name}.fromJson",TFactory) with Factory{

    val base = cla.name
    val v = MType.TMap(TString,TDynamic) ~ "value"
    val args = List(v.v)

    def createField(v:MVar.Var, index:Model):Model = {
      v.typ match {
        case SType(x) => MFunction.Call(s"${x}.fromJson",List(index)) //s"${x}.fromJson($index)"
        case _         => index
      }
    }

    val cargs = cla.vars.map(x => createField(x,MapIndex("value",x.name)))
    -->(Model.Return(MFunction.Call(s"${cla.name}",cargs)))
  }

  class To(cla:MClassProto) extends MFunction.MFunc(s"toJson",TSMap) {
    val args = List()
    def createFieldTo(v:MVar.Var):String = {
      v.typ match {
        case SType(x) => s"${v.name.string}.toJson()"
        case _ => v.name.string
      }
    }

    val cargs = cla.vars.map(x => Model.ModelTuple(x.name,createFieldTo(x)))
    val dict  = Model.Dictionary(cargs)
    -->(Model.Return(dict))
  }



}

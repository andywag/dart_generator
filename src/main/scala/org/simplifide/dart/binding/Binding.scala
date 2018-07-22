package org.simplifide.dart.binding

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import org.simplifide.template.model.MType
import org.simplifide.template.model.MVar

object Binding {

  val B_STRING = "String"
  val B_INT    = "Int"

  def opt(x:String) = s"Option[$x]"
  def rq(input:String) = input.replace(""""""","")

  def getId(json:Json) = json.hcursor.downField("id").focus.map(x => rq(x.toString()))
  def idOrBasic(json:Json):String = getId(json).getOrElse(json).toString()

  /* Get the type from the json document -
  *  Either use the string as a type name or case class should contain "id"
  * */
  def getJsonType(json:Json) = {
    val base = rq(idOrBasic(json))
    MType.decodeType(base)
  }

  def createField(json:Json,field:String) =   {
    // Get the type for the field handles lists and simple fields
    def getType(typeJson:Json) = {
      val array = typeJson.asArray
      array.map(x => {
        MType.TList(getJsonType(json))
      }).getOrElse(
        getJsonType(typeJson)
      )
    }
    val down = json.hcursor.downField(field).focus
    val newType = getType(down.get)
    MVar.Var(field,newType)
  }

  def walkField(json:Json,field:String):List[(String,MClassProto)] =
    walk(json.hcursor.downField(field).focus.get)




  private def walk(doc: Json):List[(String,MClassProto)]= {

    val arrayClasses = doc.asArray.map(x => x.flatMap(y => walk(y)))
    val arrayFilter = arrayClasses.getOrElse(Vector()).toList


    val keys = doc.hcursor.keys.getOrElse(List())
    val other = keys.filter(x => x != "id")
    val classes = arrayFilter ::: other.flatMap(walkField(doc,_)).toList


    val id = getId(doc)

    id.map(x => {
      val fields = other.map(createField(doc, _))
      val cla = (x,MClassProto.MClassProtoImpl(x, fields.toList, List()))
      cla :: classes
    }).getOrElse(classes)
  }

  def createClasses[T](input:T)(implicit encoder:Encoder[T]) = {
    walk(input.asJson).map(x => x._2)
  }

  def createClassMap[T](input:T)(implicit encoder:Encoder[T]) = {
    walk(input.asJson).toMap
  }

  sealed trait Bindings


  case class Person(x:String, d:Option[String], id:String = "Person")
  case class Result(x:String, y:Person, id:String = "Result") extends Bindings
  case class Event(name:String, event:String, results:List[Result], id:String="Event") extends Bindings


  def main(args: Array[String]): Unit = {
    val person = Person(B_STRING,Some(opt(B_STRING)))
    val result = Result("String",person)
    val event = Event("String","String",List(result))
    val json = event.asJson

    val classes = walk(json)
    println(json.noSpaces)

    val decodedFoo = decode[Event](json.noSpaces)
    println(decodedFoo)
  }


}

package org.simplifide.dart.core

import org.simplifide.dart.web.DartComponent.DartComponentFile
import org.simplifide.dart.web.DartFile
import org.simplifide.template.model.Model
import org.simplifide.template.model.dart.DartModel.{DartBuiltIn, DartPackage}

trait Importable[T] {
  def importItem(item:T, path:Option[String]):Model.Import
  def importPackage(item:T,pack:String):Model.Import
}

object Importable {


  implicit object DartBuiltInImportable extends Importable[DartBuiltIn] {
    def importItem(item:DartBuiltIn, path:Option[String]) = {
      Model.Import(item)
    }

    def importPackage(item:DartBuiltIn,pack:String):Model.Import = {
      Model.Import(item)
    }
  }


  implicit object DartFileImportable extends Importable[DartFile] {

    def importItem(item:DartFile, path:Option[String]) = {
      val dest = path.map(x => relative(item.path.get,x)).getOrElse("")
      Model.Import(dest + item.filename)
    }

    def importPackage(item:DartFile,pack:String):Model.Import = {
      val file = item.path.map(x => s"x/${item.filename}").getOrElse(item.filename)
      Model.Import(DartPackage(pack,file))
    }

  }

  // FIXME : This is creating the .template portion but it probably needs another wrapper
  implicit object DartComponentFileImportable extends Importable[DartComponentFile] {
    
    def importItem(item:DartComponentFile, path:Option[String]) = {
      val dest = path.map(x => relative(item.path.get,x)).getOrElse("")
      Model.Import(dest + item.filename.replace(".dart",".template.dart"))
    }
    
    def importPackage(item:DartComponentFile,pack:String):Model.Import = {
      val file = item.path.map(x => s"$x${item.filename}").getOrElse(item.filename)
      Model.Import(DartPackage(pack,file.replace(".dart",".template.dart")))
    }
    
  }

  implicit object PackageImportable extends Importable[DartPackage] {
    def importItem(item:DartPackage, path:Option[String]) = {
      Model.Import(item)
    }

    def importPackage(item:DartPackage,pack:String):Model.Import = {
      Model.Import(item)
    }

  }

  def i[T](x:T)(implicit converter:Importable[T], classPath:Option[String]):Model.Import = {
    converter.importItem(x,classPath)
  }

  def ip[T](pack:String, x:T)(implicit converter:Importable[T]):Model.Import = {
    converter.importPackage(x,pack)
  }

  def relative(dest:String,source:String) = {
    val s = source.split("/")
    val d = dest.split("/")

    val comb = d.zipWithIndex.map(x => {
      if (x._2 >= s.size) Some(x._1)
      else if (x._1 == s(x._2)) None //Some("..")
      else Some(x._1)
    }).flatten
    if (source == dest) ""
    else comb.mkString("/") + "/"

  }

}

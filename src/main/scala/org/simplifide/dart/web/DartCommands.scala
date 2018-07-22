package org.simplifide.dart.web

import org.simplifide.template.Constants
import org.simplifide.utils.ProcessUtil

object DartCommands {


  def build(location:String) = {
    val cmd = s"${Constants.dartLocation}\\pub.bat global run webdev build"
    ProcessUtil.runCommand(cmd,Some(location))
  }

  def setup(location:String) = {
    val cmd = s"${Constants.dartLocation}\\pub.bat get"
    ProcessUtil.runCommand(cmd,Some(location))
  }

  def run(port:Int, location:String) = {
    val cmd = s"${Constants.dartLocation}\\pub.bat global run webdev serve web:$port"
    ProcessUtil.runCommand(cmd,Some(location))
  }

}

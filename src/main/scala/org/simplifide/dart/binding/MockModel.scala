package org.simplifide.dart.binding

import org.simplifide.dart.web.DartFile


"""import 'dart:async';
 |import 'dart:convert';
 |import 'dart:math';
 |
 |import 'package:http/http.dart';
 |import 'package:http/testing.dart';
 |
 |import 'src/hero.dart';"""

case class MockModel(filename:String, service:ModelService) extends DartFile {

  IMPORT_DART_ASYNC
  IMPORT_DART_CONVERT
  IMPORT_DART_MATH
  IMPORT_HTTP
  IMPORT_PACKAGE_TESTING

}

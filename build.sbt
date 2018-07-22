name := "dart_generator"

version := "0.1"

scalaVersion := "2.12.6"

lazy val root = Project("root", file(".")) dependsOn(templateProject)
lazy val templateProject = RootProject(uri("https://github.com/andywag/template.git"))

libraryDependencies ++= Seq("org.scalatest" % "scalatest_2.12" % "3.0.5" % "test",
  "com.chuusai" %% "shapeless" % "2.3.3")
# dart_generator

This project is a generator for dart projects written in scala. The project was created to enable creating quick UIs in both web and app form 
of existing class structures stored on a server. It was created to eliminate the boilerplate required for dart and to make the code generically
support different outputs. The goal of this project was to solve a couple of repeated needs. 

* Create a simple website in a short period of time to display data (simulation results, logs, ...)
* Create a custom code generator targeted towards any language using https://github.com/andywag/template_combinator

## Status

The framework is in place to create a website based on a set of classes. This is a work in progress and not completely ready for use. Currently a basic website can be generated based on a generic set of classes but there are a few things required. 

* The scala wrapper around dart could use refactoring and cleanup for usability
* The website display portion requires work for easy use

## Usage

* There is a single test case so to run just use "sbt test" from the command line. 
* The test case is located at https://github.com/andywag/dart_generator/blob/master/src/test/scala/template/DartProjectTest.scala

*Note : This project has not been productized so will most likley not work out of the box. Most likely this will just be changing paths to the dart executable and where the test results are generated.*





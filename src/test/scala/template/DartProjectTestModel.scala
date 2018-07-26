package template

import org.simplifide.dart.binding.{Binding, MockModel, ModelService}
import org.simplifide.dart.binding.Binding.B_STRING
import org.simplifide.dart.binding.DataModel.DataModelImpl
import org.simplifide.dart.binding.ModelService.ModelServiceI

object DartProjectTestModel {

  object ProjectModel {
    import io.circe.generic.auto._

    case class Person(x: String, d: Option[String], id: String = "Person")
    case class Result(x: String, y: Person, id: String = "Result")
    case class Event(name: String, event: String, results: List[Result], id: String = "Event")

    val person = Person(B_STRING,Some(B_STRING))
    val result = Result(B_STRING,person)
    val event = Event(B_STRING,B_STRING,List(result))

    //val models = Binding.createClassMap(event).map(x => (x,DataModelImpl(x))
    val models = Binding.createClassMap(event).map(x => (x._1,DataModelImpl(x._2))).toMap

    val modelEvent = models.get(DartProjectTestModel.ProjectModel.event.id).get.cla
    val modelEventService = ModelService.all(modelEvent,"event")


    val ex_people = List.tabulate(2)(x => Person(s"person$x",None))
    val ex_result = List.tabulate(2)(x => Result(s"result$x",ex_people(x)))
    val ex_event  = List.tabulate(2)(x => Event(s"a$x",s"b$x",ex_result))

    import io.circe.syntax._
    val exJson = ex_event.asJson
    val str = exJson.toString()
    val modelEventMock    = MockModel(modelEventService, str)



  }

}

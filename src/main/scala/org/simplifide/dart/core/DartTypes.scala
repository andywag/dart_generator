package org.simplifide.dart.core

import org.simplifide.template.Template.Empty
import org.simplifide.template.model.{MFunction, MType, Model}
import org.simplifide.template.model.MType.DefinedType

object DartTypes {
  val routePath = MType.SType("RoutePath")

  object RoutePath extends DefinedType("RoutePath") {
    def apply(path:Model=Model.Empty, parent:Model=Model.Empty, useAsDefault:Model=Model.Empty, additionalData:Model=Model.Empty)= {
      create(("path",path),("parent",parent),("useAsDefault",useAsDefault),("additionalData",additionalData))
    }
  }

  object RouteDefinition extends DefinedType("RouteDefinition") {
    def apply(path:Model=Model.Empty, component:Model=Model.Empty, parent:Model=Model.Empty, useAsDefault:Model=Model.Empty,
              additionalData:Model=Model.Empty, routePath:Model=Model.Empty)= {
      create(("path",path),("parent",parent),("useAsDefault",useAsDefault),("additionalData",additionalData)
        ,("component",component), ("routePath",routePath))
    }
  }


}

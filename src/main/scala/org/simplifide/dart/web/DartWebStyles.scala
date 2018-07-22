package org.simplifide.dart.web

import scalacss.DevDefaults._  // Always use dev settings

object DartWebStyles {
  object MyInline extends StyleSheet.Standalone {

    import dsl._

    "nav a:visited, a:link"-(
      color(c"#607D8B")
    )

    "nav a:hover"-(
      color(c"#039be5"),
      backgroundColor(c"#CFD8DC")
     )

    "nav a:active"-(
      color(c"#607D8B"),
    )

    "h1"-(
      fontSize(1.2 em),
      color(c"#999"),
      marginBottom(0 ex)
    )

    "h2"-(
      fontSize(2 em),
      marginTop(0 ex),
      paddingTop(0 ex)
    )

    "nav a"- (
      padding(5 px, 10 px),
      marginTop(10 px),
      //display() // display: inline-block;
      backgroundColor(c"#eee"),
      borderRadius(4 px)
    )


  }

}

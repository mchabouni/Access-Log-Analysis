package com.ebiznext.accesslog.model


case class Attribute(
                      name: String,
                      `type`: String = "string",
                      array: Option[Boolean] = None,
                      required: Boolean = true,
                      attributes: Option[List[Attribute]] = None
                    ) {


  def mapping(): String = {
    attributes match {
      case Some(attrs) =>
        s"""
           |"$name": {
           |  "properties" : {
           |  ${attrs.map(_.mapping()).mkString(",")}
           |  }
           |}""".stripMargin

      case None => {
        val typeMapping=IndexMapping.fromType(new PrimitiveTypeDeserializer().simpleTypeFromString(this.`type`)).toString
        s"""
           |"$name": {
           |  "type": "$typeMapping"
           |}""".stripMargin
      }

    }
  }
}
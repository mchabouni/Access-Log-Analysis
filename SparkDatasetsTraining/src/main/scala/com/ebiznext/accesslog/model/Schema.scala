package com.ebiznext.accesslog.model

import java.util.regex.Pattern

import org.apache.spark.sql.types._

case class Schema (
                    name: String,
                    attributes: List[Attribute]
                  )  {

  def mapping(template: Option[String], domainName: String): String = {
    val attrs = attributes.map(_.mapping()).mkString(",")
    val properties =
      s"""
         |"properties": {
         |$attrs
         |}""".stripMargin

    template.getOrElse {
      s"""
         |{
         |  "index_patterns": ["${domainName}_$name", "${domainName}_$name-*"],
         |  "settings": {
         |    "number_of_shards": "1",
         |    "number_of_replicas": "0"
         |  },
         |  "mappings": {
         |    "_doc": {
         |      "_source": {
         |        "enabled": true
         |      },
         |__PROPERTIES__
         |    }
         |  }
         |}""".stripMargin.replace("__PROPERTIES__", properties)
    }

  }
}

object Schema {

  def mapping(domainName: String, schemaName: String, obj: StructField): String = {
    def buildAttributeTree(obj: StructField): Attribute = {
      obj.dataType match {
        case StringType | LongType | ShortType | DoubleType | BooleanType | ByteType |
             DateType | TimestampType =>
          Attribute(obj.name, obj.dataType.typeName, required = !obj.nullable)
        //add case IntegerType to pass "int" primitive type instead of  "integer"
        case IntegerType                      => Attribute(obj.name, "int", required = !obj.nullable)
        case d: DecimalType                   => Attribute(obj.name, "decimal", required = !obj.nullable)
        case ArrayType(eltType, containsNull) => buildAttributeTree(obj.copy(dataType = eltType))
        case x: StructType =>
          new Attribute(
            obj.name,
            "struct",
            required = !obj.nullable,
            attributes = Some(x.fields.map(buildAttributeTree).toList)
          )
        case _ => throw new Exception(s"Unsupported Date type ${obj.dataType} for object $obj ")
      }
    }

    Schema(
      schemaName,
      buildAttributeTree(obj).attributes.getOrElse(Nil)
    ).mapping(None, domainName)
  }
}


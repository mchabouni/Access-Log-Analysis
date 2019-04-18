
import sbt._

object Dependencies {

  val jacksonExclusions = Seq(
    ExclusionRule(organization = "com.fasterxml.jackson.core"),
    ExclusionRule(organization = "org.codehaus.jackson")
  )

  val scalaTest = Seq(
    "org.scalatest" %% "scalatest" % Versions.scalatest,
    "org.scalatest" %% "scalatest" % Versions.scalatest % "test"
  )

  val logging = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % Versions.scalaLogging
  )

  val typedConfigs = Seq("com.github.kxbmap" %% "configs" % Versions.configs)
  
  val jackson = Seq(
    "com.fasterxml.jackson.core" % "jackson-core" % Versions.jackson,
    "com.fasterxml.jackson.core" % "jackson-annotations" % Versions.jackson,
    "com.fasterxml.jackson.core" % "jackson-databind" % Versions.jackson,
    "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % Versions.jackson
  )
  val spark = Seq(
    "org.apache.spark" %% "spark-core" % Versions.spark  % "provided",
    "org.apache.spark" %% "spark-sql" % Versions.spark % "provided",
    "org.apache.spark" %% "spark-hive" % Versions.spark % "provided"
  )


  val dependencies = spark ++ jackson ++ scalaTest ++ logging ++ typedConfigs

}

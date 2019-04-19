import sbt._

object Dependencies {

  lazy val commonDependencies =Seq(
    "org.scalatest" %% "scalatest" % Versions.scalatest,
    "org.scalatest" %% "scalatest" % Versions.scalatest % "test"
  )
  
  lazy val jacksonExclusions = Seq(
    ExclusionRule(organization = "com.fasterxml.jackson.core"),
    ExclusionRule(organization = "org.codehaus.jackson")
  )

  lazy val sparkDataTrainDependencies=Seq(
    "org.elasticsearch" % "elasticsearch-hadoop" % Versions.esHadoop,
    "com.typesafe.scala-logging" %% "scala-logging" % Versions.scalaLogging,
    "com.github.kxbmap" %% "configs" % Versions.configs,
    "com.fasterxml.jackson.core" % "jackson-core" % Versions.jackson,
    "com.fasterxml.jackson.core" % "jackson-annotations" % Versions.jackson,
    "com.fasterxml.jackson.core" % "jackson-databind" % Versions.jackson,
    "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % Versions.jackson,
    "org.apache.spark" %% "spark-core" % Versions.spark ,
    "org.apache.spark" %% "spark-sql" % Versions.spark
  )

  lazy val accessLogParserDependencies=Seq(
    "com.snowplowanalytics" %% "scala-maxmind-iplookups" % Versions.mmIpLookup,
    "org.typelevel"         %% "cats-effect"          % "0.10.1",
    "org.typelevel"         %% "cats-core"            % "1.1.0",
    "org.apache.spark" %% "spark-sql" % Versions.spark 
  )
  
  lazy val userAgentParserDependencies=Seq(
    "org.yaml" % "snakeyaml" % Versions.snakeyaml,
    "org.specs2" %% "specs2-core" % Versions.specs2core % "test"
  )
}

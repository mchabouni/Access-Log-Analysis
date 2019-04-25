package com.ebiznext.accesslog.job

import com.ebiznext.accesslog.conf.Settings
import com.ebiznext.accesslog.model.Schema
import com.softwaremill.sttp._
import com.softwaremill.sttp.HttpURLConnectionBackend
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.{Dataset, SaveMode}
import org.elasticsearch.spark.sql._

/**
  *
  * @param data DataSet to index into ES
  * @param index name of the index
  * @param `type` name of the type
  * @tparam T Generic Type of the input Dataset content
  */
class IndexJob [T] (data: Dataset[T],domain:String,schema:String) extends SparkJob {
  val indexName = s"${domain}_$schema".toLowerCase()
  val typeName = s"${domain}_$schema".toLowerCase()
  val resource = s"${indexName}/_doc"
  override def name: String = s"Hadoop Data to ES index job: ${indexName}"


  val esresource = Some(("es.resource.write", s"${resource}"))
  val esCliConf = List(esresource).flatten.toMap

  /**
    * Main entry point as required by the Spark Job interface
    *
    * @return : Spark Session used for the job
    */

  def run() ={
    logger.info("===Start "++name++"===")
    val content=Schema.mapping(domain.toLowerCase(),schema.toLowerCase(), StructField("ignore", data.schema))
    logger.info(s"Registering template ${domain}_${schema} -> $content")
    import scala.collection.JavaConverters._
    val esOptions = Settings.sparktrain.elasticsearch.options.asScala.toMap
    val host: String = esOptions.getOrElse("es.nodes", "localhost")
    val port = esOptions.getOrElse("es.port", "9200").toInt
    val ssl = esOptions.getOrElse("es.net.ssl", "false").toBoolean
    val protocol = if (ssl) "https" else "http"

    implicit val backend = HttpURLConnectionBackend()

    val requestDel = sttp
      .body(content)
      .delete(uri"$protocol://$host:$port/_template/${domain.toLowerCase()}_${schema.toLowerCase()}")
      .contentType("application/json")
    val responseDel = requestDel.send()


    val requestPut = sttp
      .body(content)
      .put(uri"$protocol://$host:$port/_template/${domain.toLowerCase()}_${schema.toLowerCase}")
      .contentType("application/json")

    val responsePut = requestPut.send()
    val ok = (200 to 299) contains responsePut.code
    println(responsePut.body)
    if (ok) {
      val allConf = esOptions.toList ++ esCliConf.toList
      logger.info(s"sending ${data.count()} documents to Elasticsearch using $allConf")
      allConf
        .foldLeft(data.write)((w, kv) => w.option(kv._1, kv._2))
        .format("org.elasticsearch.spark.sql")
        .mode(SaveMode.Overwrite)
        .save(resource)
    } else {
      logger.error("Failed to create template")
    }




    logger.info("===Finished "++name++"===")
    sparkSession

  }


}

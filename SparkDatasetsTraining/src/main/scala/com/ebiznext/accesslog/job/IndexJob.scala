package com.ebiznext.accesslog.job

import org.apache.spark.sql.Dataset
import org.elasticsearch.spark.sql._

/**
  *
  * @param data DataSet to index into ES
  * @param index name of the index
  * @param `type` name of the type
  * @tparam T Generic Type of the input Dataset content
  */
class IndexJob [T] (data: Dataset[T],index:String,`type`:String) extends SparkJob {

  override def name: String = "Hadoop Data to ES index job: "+index+"/"+`type`

  /**
    * Main entry point as required by the Spark Job interface
    *
    * @return : Spark Session used for the job
    */

  def run() ={
    logger.info("===Start "++name++"===")

    EsSparkSQL.saveToEs(data,index+"/"+`type`)

    logger.info("===Finished "++name++"===")
    sparkSession
  }

}

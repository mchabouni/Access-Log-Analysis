package com.ebiznext.sparktrain.job

import org.apache.spark.sql.Dataset
import org.elasticsearch.spark.sql._

class IndexJob [T] (data: Dataset[T],index:String,`type`:String) extends SparkJob {

  override def name: String = "Hadoop Data to ES index job: "+index+"/"+`type`

  def run() ={
    EsSparkSQL.saveToEs(data,index+"/"+`type`)
    sparkSession
  }

}

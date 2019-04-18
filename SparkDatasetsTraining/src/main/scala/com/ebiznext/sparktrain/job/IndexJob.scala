package com.ebiznext.sparktrain.job

import org.apache.spark.sql.Dataset
import org.elasticsearch.spark.sql._

class IndexJob [T] (data: Dataset[T]) extends SparkJob {

  override def name: String = "Hadoop Data ES index job"

  def run() ={
    EsSparkSQL.saveToEs(data,"spark/docs")
    sparkSession
  }

}

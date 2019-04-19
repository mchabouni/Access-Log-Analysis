package com.ebiznext.accesslog.conf

import org.apache.spark.SparkConf
import com.ebiznext.accesslog.conf.HdfsConf._
import org.apache.spark.sql.SparkSession

class SparkEnv (name: String) {

  val conf: SparkConf = new SparkConf()
                        .setAppName("Spark training exercises")
                        .set("spark.sql.files.maxPartitionBytes","10000000")
                        .setMaster("local[*]")
                        .set("fs.defaultFS", load().get("fs.defaultFS"))

  lazy val session: SparkSession = SparkSession.builder.config(conf).getOrCreate()
}

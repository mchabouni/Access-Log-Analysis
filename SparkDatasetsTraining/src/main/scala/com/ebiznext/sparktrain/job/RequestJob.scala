package com.ebiznext.sparktrain.job
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

trait RequestJob extends SparkJob {


  def run():SparkSession
}

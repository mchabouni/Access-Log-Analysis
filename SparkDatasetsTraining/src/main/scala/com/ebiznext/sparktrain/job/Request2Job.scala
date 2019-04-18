package com.ebiznext.sparktrain.job

import org.apache.spark.sql.SparkSession

class Request2Job (device:String) extends RequestJob {
  override val name="Request 2 Job: Top 3 visited URI per country"

  def run(): SparkSession ={

    sparkSession
  }
}

package com.ebiznext.sparktrain.job
import com.alvinalexander.accesslogparser.AccessLogRecord
import com.ebiznext.sparktrain.conf.Settings
import com.ebiznext.sparktrain.data.IOJob._
import com.ebiznext.sparktrain.handler.GeoDataHandler._
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

trait RequestJob extends SparkJob {

  def getAllData() ={
    import sparkSession.implicits._
    val accessLogDs: Dataset[AccessLogRecord] =read(new Path(Settings.sparktrain.inputPath++"accesslog2000.log"))
    val demographicsDs=readCSV(new Path(Settings.sparktrain.inputPath++"population_per_country_2017.csv"),";")
    val addCountryColUDF = udf((col:String)=>findIp(col).countryName)
    accessLogDs.withColumn("country",addCountryColUDF($"clientIpAddress"))
                .join(demographicsDs,$"country"===$"countryName")
  }

  def run():SparkSession
}

package com.ebiznext.accesslog.job

import com.alvinalexander.accesslogparser.AccessLogRecord
import com.ebiznext.accesslog.conf.Settings
import com.ebiznext.accesslog.io.{IngestAccessLogRecJob}
import com.ebiznext.accesslog.io.WriteJob._
import com.ebiznext.accesslog.model.Request2Record
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{count, dense_rank}
import org.apache.spark.sql.{Dataset, SparkSession}

class Request2Job(os: Option[String] = None, browser: Option[String] = None) extends SparkJob {

  override val name = "Request 2 Job: Top 3 httpResponse by country given OS or browser or both "

  import sparkSession.implicits._
  val accessLogDs: Dataset[AccessLogRecord] = IngestAccessLogRecJob.read(new Path(Settings.sparktrain.inputPath ++ "access.log"))

  /**
    * Compute Top 3 httpResponse by country given OS or browser or both
    * @param filterDf filtered Dataset to execute request on
    */
  def executeRequest(filterDf: Dataset[AccessLogRecord]) {
    val df = filterDf
      .select($"country", $"httpStatusCode")
      .groupBy($"country", $"httpStatusCode")
      .agg(count("*") as "count")

    val countWin = Window
      .partitionBy($"country")
      .orderBy($"count".desc)

    val dataSave = df
      .withColumn("rank", dense_rank.over(countWin))
      .where($"rank" <= 3)
      .as[Request2Record]
    write(new Path(Settings.sparktrain.savePath ++ "request2/"), dataSave)
  }

  /**
    * Main entry point as required by the Spark Job interface
    * perform a pattern matching on class attributes to call executeRequest with the right parameters
    * @return : Spark Session used for the job
    */
  def run(): SparkSession = {
    logger.info("===Start "++name++"===")

    (os, browser) match {
      case (Some(os), Some(browser)) =>
        executeRequest(
          accessLogDs.where(
            $"userAgent.userAgent.family" === browser && $"userAgent.os.family"
              .like(os + "%")))

      case (Some(os), None) =>
        executeRequest(accessLogDs.where($"userAgent.os.family".like(os + "%")))

      case (None, Some(browser)) =>
        executeRequest(
          accessLogDs.where($"userAgent.userAgent.family" === browser))

      case _ => logger.error("Specify at least a browser or a OS")
    }

    logger.info("===Finished "++name++"===")
    sparkSession
  }
}

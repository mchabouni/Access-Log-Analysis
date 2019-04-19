package com.ebiznext.sparktrain.job

import com.alvinalexander.accesslogparser.AccessLogRecord
import com.ebiznext.sparktrain.conf.Settings
import org.apache.hadoop.fs.Path
import com.ebiznext.sparktrain.io.{IngestAccessLogRecJob,IngestDemographicsJob}
import com.ebiznext.sparktrain.io.WriteJob._
import com.ebiznext.sparktrain.model.Request1Record
import org.apache.spark.sql.{DataFrame, Dataset}
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{count, dense_rank, sum}

object Request1Job extends SparkJob {
    override val name="Request 1 Job: Top 3 visited URI per country"

    def run() ={
        import sparkSession.implicits._
        val demographicsDs=IngestDemographicsJob.read(new Path(Settings.sparktrain.inputPath++"population_per_country_2017.csv"))
        val accessLogDs =IngestAccessLogRecJob.read(new Path(Settings.sparktrain.inputPath++"accesslog2000.log"))

        val df=accessLogDs.select($"request.uri",$"country")
                            .groupBy($"uri",$"country")
                            .agg(count("*") as "count")
                            .join(demographicsDs,"country")

        val countWin = Window.partitionBy($"country")
          .orderBy($"count".desc)

        val dataSave=df.withColumn("rank", dense_rank.over(countWin))
                        .where($"rank"<=3).as[Request1Record]

        write(new Path(Settings.sparktrain.savePath++"request1/"),dataSave)
        sparkSession
    }

}

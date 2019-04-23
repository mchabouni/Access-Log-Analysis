package com.ebiznext.accesslog.job

import com.alvinalexander.accesslogparser.AccessLogRecord
import com.ebiznext.accesslog.conf.Settings
import org.apache.hadoop.fs.Path
import com.ebiznext.accesslog.io.WriteJob._
import com.ebiznext.accesslog.model.{Demographics, Request1Record}
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{count, dense_rank}

class Request1Job (demographicsDs: Dataset[Demographics],accessLogDs: Dataset[AccessLogRecord]) extends SparkJob {
    override val name="Request 1 Job: Top 3 visited URI per country"

    /**
      * Main entry point as required by the Spark Job interface
      * The job compute Top 3 visited URI per country and store result to HDFS as parquet
      * @return : Spark Session used for the job
      */
    def run() ={
        logger.info("===Start "++name++"===")

        import sparkSession.implicits._

        val df=accessLogDs.select($"request.uri",$"country")
                            .groupBy($"uri",$"country")
                            .agg(count("*") as "count")
                            .join(demographicsDs,"country")

        val countWin = Window.partitionBy($"country")
          .orderBy($"count".desc)

        val dataSave=df.withColumn("rank", dense_rank.over(countWin))
                        .where($"rank"<=3).as[Request1Record]

        write(new Path(Settings.sparktrain.savePath++"request1/"),dataSave)

        logger.info("===Finished "++name++"===")
        sparkSession
    }

}

package com.ebiznext.sparktrain.job

import com.ebiznext.sparktrain.conf.Settings
import org.apache.hadoop.fs.Path
import com.ebiznext.sparktrain.data.IOJob._
import com.ebiznext.sparktrain.data.OutputData.Request1Record
import org.apache.spark.sql.{DataFrame, Dataset}
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{count, dense_rank, sum}

object Request1Job extends RequestJob {
    override val name="Request 1 Job: Top 3 visited URI per country"

    def run() ={
        import sparkSession.implicits._

        val df=getAllData().select($"request.uri",$"country",$"population")
                            .groupBy($"uri",$"country",$"population").agg(count("*") as "count")

        val countWin = Window.partitionBy($"country")
          .orderBy($"count".desc)

        val dataSave=df.withColumn("rank", dense_rank.over(countWin))
                        .where($"rank"<=3).as[Request1Record]

        write(new Path(Settings.sparktrain.savePath++"request1/"),dataSave)
        sparkSession
    }

}

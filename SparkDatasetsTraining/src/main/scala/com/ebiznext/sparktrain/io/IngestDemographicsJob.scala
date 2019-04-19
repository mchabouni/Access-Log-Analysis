package com.ebiznext.sparktrain.io

import com.ebiznext.sparktrain.conf.SparkEnv
import com.ebiznext.sparktrain.model.Demographics
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.{Dataset, Encoder}

object IngestDemographicsJob extends IngestionJob[Demographics] {

  def read(path: Path) :Dataset[Demographics]={
    val enc: Encoder[Demographics] = ExpressionEncoder()
    val sparkenv:SparkEnv=new SparkEnv("ReadJob"++ path.getName)
    sparkenv.session.read.option("header", "true").option("delimiter",";").option("inferSchema","true").csv(path.toString).as[Demographics](enc)
  }

}

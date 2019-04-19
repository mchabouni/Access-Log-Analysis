package com.ebiznext.accesslog.io

import com.ebiznext.accesslog.conf.SparkEnv
import com.ebiznext.accesslog.model.Demographics
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.{Dataset, Encoder}

object IngestDemographicsJob extends IngestionJob[Demographics] {
  /**
    * Read the Dataworldbank population per country CSV
    * @param path of the CSV file
    * @return Dataset of Demographics
    */
  def read(path: Path) :Dataset[Demographics]={
    val enc: Encoder[Demographics] = ExpressionEncoder()
    val sparkenv:SparkEnv=new SparkEnv("ReadJob"++ path.getName)
    sparkenv.session.read.option("header", "true").option("delimiter",";").option("inferSchema","true").csv(path.toString).as[Demographics](enc)
  }

}

package com.ebiznext.sparktrain.data

import com.alvinalexander.accesslogparser.{AccessLogParser, AccessLogRecord}
import com.ebiznext.sparktrain.conf.SparkEnv
import com.ebiznext.sparktrain.data.InputData.Demographics
import com.ebiznext.sparktrain.handler.HdfsHandler._
import com.typesafe.scalalogging.StrictLogging
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql._

object IOJob extends StrictLogging{
  /**
    * Reads the csv files specified in the path into DataSet
    * @param path path to start reading files from
    * @return DataSet of AccessLogRecord corresponding to the read files.
    */
  def read(path: Path): Dataset[AccessLogRecord] = {
    val enc: Encoder[AccessLogRecord] = ExpressionEncoder()
    val parser=new AccessLogParser
    val sparkenv:SparkEnv=new SparkEnv("ReadJob"++ path.getName)
    sparkenv.session.read.textFile(path.toString).map(parser.parseRecordReturningNullObjectOnFailure(_))(enc).as[AccessLogRecord](enc)
  }

  def readCSV (path: Path,separator:String) :Dataset[Demographics]={
    val enc: Encoder[Demographics] = ExpressionEncoder()
    val sparkenv:SparkEnv=new SparkEnv("ReadJob"++ path.getName)
    sparkenv.session.read.option("header", "true").option("delimiter",separator).option("inferSchema","true").csv(path.toString).as[Demographics](enc)
  }

  /**
    * Saves a DataSet of generic type T into a parquet file
    * @param path path to save the dataset in
    * @param df   DataSet to save
    * @return boolean corresponds to whether or not the path exists
    */
  def write[T](path: Path, ds: Dataset[T]): Boolean = {
    ds.write.mode(SaveMode.Overwrite).parquet(path.toString)
    exists(path)
  }


  def partionnedWrite[T] (savepath: Path, ds: Dataset[T]): Boolean ={
    write[T](savepath,ds)

    val rapport=(spaceConsumed(savepath)/blockSize(savepath)).toInt

    val nbpart = rapport match {
      case 0 => 1
      case _ => rapport
    }
    logger.info(s"nb partitions: $nbpart")
    ds.coalesce(nbpart).write.mode(SaveMode.Overwrite).parquet(savepath.toString)
    exists(savepath)
  }

}

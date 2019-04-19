package com.ebiznext.sparktrain.io

import com.ebiznext.sparktrain.handler.HdfsHandler.{blockSize, exists, spaceConsumed}
import com.typesafe.scalalogging.StrictLogging
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.{Dataset, SaveMode}

object WriteJob extends StrictLogging{

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
    val dsSampled=ds.sample(0.08)
    write[T](savepath,dsSampled)

    val ratio=(spaceConsumed(savepath)/blockSize(savepath)).toInt

    val nbpart = Math.max(ratio,1)
    logger.info(s"nb partitions: $nbpart")
    ds.coalesce(nbpart).write.mode(SaveMode.Overwrite).parquet(savepath.toString)
    exists(savepath)
  }
}

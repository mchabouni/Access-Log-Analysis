package com.ebiznext.sparktrain.io
import com.ebiznext.sparktrain.handler.HdfsHandler.{blockSize, exists, spaceConsumed}
import com.typesafe.scalalogging.StrictLogging
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.{Dataset, SaveMode}


trait IngestionJob [T] extends StrictLogging{
  def read(path: Path): Dataset[T]
}

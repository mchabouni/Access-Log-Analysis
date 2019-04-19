package com.ebiznext.accesslog.io

import com.alvinalexander.accesslogparser.{AccessLogParser, AccessLogRecord}
import com.ebiznext.accesslog.conf.SparkEnv
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.{Dataset, Encoder}

object IngestAccessLogRecJob extends IngestionJob[AccessLogRecord] {
  /**
    * Reads the access.log file and parse each line
    * @param path path to start reading files from
    * @return DataSet of AccessLogRecord.
    */
  def read(path: Path): Dataset[AccessLogRecord] = {
    val enc: Encoder[AccessLogRecord] = ExpressionEncoder()
    val parser=new AccessLogParser
    val sparkenv:SparkEnv=new SparkEnv("ReadJob"++ path.getName)
    sparkenv.session.read.textFile(path.toString).map(parser.parseRecordReturningNullObjectOnFailure(_))(enc).as[AccessLogRecord](enc)
  }

}

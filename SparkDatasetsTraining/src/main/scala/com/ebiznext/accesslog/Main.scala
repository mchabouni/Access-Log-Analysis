/*
 *
 *  * Licensed to the Apache Software Foundation (ASF) under one or more
 *  * contributor license agreements.  See the NOTICE file distributed with
 *  * this work for additional information regarding copyright ownership.
 *  * The ASF licenses this file to You under the Apache License, Version 2.0
 *  * (the "License"); you may not use this file except in compliance with
 *  * the License.  You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 *
 */

package com.ebiznext.accesslog
import java.io.File

import cats.effect.IO
import com.alvinalexander.accesslogparser.{AccessLogParser, AccessLogRecord}
import com.ebiznext.accesslog.conf.Settings
import com.ebiznext.accesslog.io.{IngestAccessLogRecJob, IngestDemographicsJob}
import com.ebiznext.accesslog.job._
import com.ebiznext.accesslog.model.Demographics
import com.snowplowanalytics.maxmind.iplookups.IpLookups
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.functions._

import scala.io.{BufferedSource, Source}



object Main {



  def main(args: Array[String]): Unit = {

    val accessLogDs: Dataset[AccessLogRecord] =IngestAccessLogRecJob.read(new Path(Settings.sparktrain.logsPath))
    val demographicsDs: Dataset[Demographics] =IngestDemographicsJob.read(new Path(Settings.sparktrain.demosPath))


    val rq1= new Request1Job(demographicsDs,accessLogDs)
    rq1.run()

    val rq2=new Request2Job(accessLogDs,browser = Some("Chrome"),os = Some("Mac"))
    rq2.run()

    val index=new IndexJob[AccessLogRecord](accessLogDs,"accesslog","access")
    index.run()


  }
}

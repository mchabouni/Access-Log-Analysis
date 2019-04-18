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

package com.ebiznext.sparktrain
import java.io.File

import cats.effect.IO
import com.alvinalexander.accesslogparser.{AccessLogParser, AccessLogRecord}
import com.ebiznext.sparktrain.conf.Settings
import com.ebiznext.sparktrain.job.{IndexJob, Request1Job}
import com.ebiznext.sparktrain.data.IOJob._
import com.ebiznext.sparktrain.data.InputData.Demographics
import com.ebiznext.sparktrain.handler.GeoDataHandler
import com.snowplowanalytics.maxmind.iplookups.IpLookups
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.Dataset

import scala.io.{BufferedSource, Source}



object Main {



  def main(args: Array[String]): Unit = {
    //val data="""122.165.54.17 - - [21/Jul/2009:02:48:15 -0700] "GET /java/java_oo/java_oo.css HTTP/1.1" 200 1235 "http://www.devdaily.com/java/java_oo/" "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9B206 Safari/7534.48.3""""
    //val parser= new AccessLogParser
    ///val rec = parser.parseRecord(data)
    //println(rec)
    //val parser=new AccessLogParser
    //val ds=read(new Path(Settings.sparktrain.inputPath))
    //ds.show()
   // val ds2=readCSV(new Path(Settings.sparktrain.inputPath),";")
    //ds2.show()
    //Request1Job.run()
    val accessLogDs: Dataset[AccessLogRecord] =read(new Path(Settings.sparktrain.inputPath++"accesslog2000.log"))
    val index=new IndexJob[AccessLogRecord](accessLogDs)
    index.run()
    //write(new Path(Settings.sparktrain.savePath),ds)


   // println(GeoDataHandler.findIp("41.109.118.2"))


  }
}

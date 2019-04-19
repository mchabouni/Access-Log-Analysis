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

package com.ebiznext.accesslog.job
import com.ebiznext.accesslog.conf.SparkEnv
import com.typesafe.scalalogging.StrictLogging
import org.apache.spark.sql.SparkSession




trait SparkJob extends StrictLogging{
  def name: String

  lazy val sparkEnv: SparkEnv = new SparkEnv(name)
  lazy val sparkSession: SparkSession = sparkEnv.session


}

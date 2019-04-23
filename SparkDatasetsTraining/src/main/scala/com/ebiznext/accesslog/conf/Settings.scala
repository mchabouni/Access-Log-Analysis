package com.ebiznext.accesslog.conf

import com.typesafe.config.{Config, ConfigFactory}
import configs.syntax._


object Settings {
  final case class SparkTraining(
                                  logsPath:String,
                                  demosPath:String,
                                  savePath :String
                                )

  val config: Config = ConfigFactory.load()

  val sparktrain: SparkTraining = config.extract[SparkTraining].valueOrThrow{ error =>
    error.messages.foreach(println)
    throw new Exception("Failed to load config")
  }


}

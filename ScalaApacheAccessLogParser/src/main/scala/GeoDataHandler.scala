package com.alvinalexander.accesslogparser

import cats.effect.IO
import com.snowplowanalytics.maxmind.iplookups.model.IpLocation
import com.snowplowanalytics.maxmind.iplookups.{IpLookups, model}

object GeoDataHandler {

  def findIp(ip: String):model.IpLocation ={
    val result=(for {
                      ipLookups <- IpLookups.createFromFilenames[IO](
                        geoFile = Some(getClass.getResource("/GeoLite2-City.mmdb").getFile),
                        ispFile = None,
                        domainFile = None,
                        connectionTypeFile = None,
                        memCache = false,
                        lruCacheSize = 20000
                      )

                      lookup <- ipLookups.performLookups(ip)
                    } yield lookup).unsafeRunSync()

    result.ipLocation match {
      case Some(Right(loc)) =>
        loc
      case _ =>
        IpLocation(null,null,None,None,0.0f,0.0f,None,None,None,None)
    }

  }

}

package com.alvinalexander.accesslogparser

import org.uaparser.scala.Client

/**
 * @see http://httpd.apache.org/docs/2.2/logs.html for details
 */
case class AccessLogRecord (
                             clientIpAddress: String, // should be an ip address, but may also be the hostname if hostname-lookups are enabled
                             rfc1413ClientIdentity: String, // typically `-`
                             remoteUser: String, // typically `-`
                             dateTime: String, // [day/month/year:hour:minute:second zone]
                             request: Option[Request], // `GET /foo ...`
                             httpStatusCode: String, // 200, 404, etc.
                             bytesSent: String, // may be `-`
                             referer: String, // where the visitor came from
                             userAgent: Client // long string to represent the browser and OS
)
















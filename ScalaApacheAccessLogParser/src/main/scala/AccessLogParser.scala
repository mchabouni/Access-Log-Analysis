package com.alvinalexander.accesslogparser

import org.joda.time.DateTime
import java.util.regex.Pattern
import java.text.SimpleDateFormat
import java.util.Locale
import scala.util.control.Exception._
import java.util.regex.Matcher

import GeoDataHandler._
import org.uaparser.scala.UserAgent.UserAgentParser
import org.uaparser.scala._

import scala.util.{Failure, Success, Try}

/**
 * A sample record:
 * 94.102.63.11 - - [21/Jul/2009:02:48:13 -0700] "GET / HTTP/1.1" 200 18209 "http://acme.com/foo.php" "Mozilla/4.0 (compatible; MSIE 5.01; Windows NT 5.0)"
 * 
 * I put this code in the 'class' so (a) the pattern could be pre-compiled and (b) the user can create
 * multiple instances of this parser, in case they want to work in a multi-threaded way.
 * I don't know that this is necessary, but I think it is for this use case.
 * 
 */

@SerialVersionUID(100L)
class AccessLogParser extends Serializable {

    private val ddd = "\\d{1,3}"                      // at least 1 but not more than 3 times (possessive)
    private val ip = s"($ddd\\.$ddd\\.$ddd\\.$ddd)?"  // like `123.456.7.89`
    private val client = "(\\S+)"                     // '\S' is 'non-whitespace character'
    private val user = "(\\S+)"
    private val dateTime = "(\\[.+?\\])"              // like `[21/Jul/2009:02:48:13 -0700]`
    private val request = "\"(.*?)\""                 // any number of any character, reluctant
    private val status = "(\\d{3})"
    private val bytes = "(\\S+)"                      // this can be a "-"
    private val referer = "\"(.*?)\""
    private val agent = "\"(.*?)\""
    private val regex = s"$ip $client $user $dateTime $request $status $bytes $referer $agent"
    private val p = Pattern.compile(regex)
    private val uaParser=Parser.default
    /**
     * note: group(0) is the entire record that was matched (skip it)
     * @param record Assumed to be an Apache access log combined record.
     * @return An AccessLogRecord instance wrapped in an Option.
     */
    def parseRecord(record: String): Option[AccessLogRecord] = {
        val matcher = p.matcher(record)
        if (matcher.find) {
            Some(buildAccessLogRecord(matcher))
        } else {
            None
        }
    }

    /**
     * Same as parseRecord, but returns a "Null Object" version of an AccessLogRecord
     * rather than an Option.
     * 
     * @param record Assumed to be an Apache access log combined record.
     * @return An AccessLogRecord instance. This will be a "Null Object" version of an
     * AccessLogRecord if the parsing process fails. All fields in the Null Object
     * will be empty strings.
     */
    def parseRecordReturningNullObjectOnFailure(record: String): AccessLogRecord = {
        val matcher = p.matcher(record)
        if (matcher.find) {
            buildAccessLogRecord(matcher)
        } else {
            AccessLogParser.nullObjectAccessLogRecord
        }
    }
    
    private def buildAccessLogRecord(matcher: Matcher) = {
        AccessLogRecord(
            matcher.group(1),
            findIp(matcher.group(1)).countryName,
            matcher.group(2),
            matcher.group(3),
            AccessLogParser.parseDateField(matcher.group(4)),
            AccessLogParser.parseRequestField(matcher.group(5)),
            matcher.group(6),
            matcher.group(7),
            matcher.group(8),
            uaParser.parse(matcher.group(9)))
    }
}

/**
 * A sample record:
 * 94.102.63.11 - - [21/Jul/2009:02:48:13 -0700] "GET / HTTP/1.1" 200 18209 "http://acme.com/foo.php" "Mozilla/4.0 (compatible; MSIE 5.01; Windows NT 5.0)"
 */
object AccessLogParser {

    val nullObjectAccessLogRecord = AccessLogRecord("","", "", "", None,None, "", "", "",Client(UserAgent("",None,None,None),OS("",None,None,None,None),Device("",None,None)) )
    
    /**
     * @param A String like "GET /the-uri-here HTTP/1.1"
     * @return A Tuple3(requestType, uri, httpVersion). requestType is GET, POST, etc.
     * 
     * Returns a Tuple3 of three blank strings if the method fails.
     */
    def parseRequestField(request: String): Option[Request] = {
        val arr = request.split(" ")
        if (arr.size == 3) Some(Request(arr(0), arr(1), arr(2))) else None
    }
    
    /**
     * @param A String that looks like "[21/Jul/2009:02:48:13 -0700]"
     */
    def parseDateField(field: String): Option[String] = {
        val dateRegex = "\\[(.*?) .+]"
        val datePattern = Pattern.compile(dateRegex)
        val dateMatcher = datePattern.matcher(field)
        if (dateMatcher.find) {
                val dateString = dateMatcher.group(1)
                // HH is 0-23; kk is 1-24
                val dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH)
                allCatch.opt(new DateTime(dateFormat.parse(dateString).getTime).toString)
            } else {
            None
        }
    }
}





package com.ebiznext.accesslog.model

/**
  *
  * @param country          :Country Name
  * @param httpStatusCode   : HttpStatusCode in response of the client request
  * @param count            : Number of time we got this response in this country (with the given OS and browser in the job)
  * @param rank             : rank of this http response in this country
  */
case class Request2Record(
                           country:String,
                           httpStatusCode:String,
                           count:Long,
                           rank:Int
                         )

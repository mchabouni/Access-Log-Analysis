package com.ebiznext.accesslog.model

/**
  *
  * @param uri        : website Uri
  * @param country    : Country name
  * @param count      : number of visits
  * @param population : Population of the country
  * @param rank       : rank of the website uri (based on number of visits) in this country
  */
case class Request1Record(
                           uri:String,
                           country:String,
                           count:Long,
                           population:Long,
                           rank:Int
                         )

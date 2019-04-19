package com.ebiznext.sparktrain.model

/**
  *
  * @param uri
  * @param countryName
  * @param population
  */
case class Request1Record(
                           uri:String,
                           country:String,
                           count:Long,
                           population:Long,
                           rank:Int
                         )

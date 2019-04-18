package com.ebiznext.sparktrain.data

object OutputData {

  /**
    *
    * @param uri
    * @param countryName
    * @param population
    */
  case class Request1Record(
                             uri:String,
                             country:String,
                             population:Long,
                             count:Long,
                             rank:Long
                           )


}

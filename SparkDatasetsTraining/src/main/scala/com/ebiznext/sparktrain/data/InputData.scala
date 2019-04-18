package com.ebiznext.sparktrain.data

import java.sql.Date

object InputData {
  /** Revenue per product parsing case class
    * @param transaction_dt               : Transaction Id
    * @param customer_id                  : Customer Id
    * @param age_group                    : Age group of the customer
    * @param pin_code                     :
    * @param product_subclass             : Produuct category
    * @param product_id                   : Product Id
    * @param amount                       : Amount of product selled in the transaction
    * @param asset                        :
    * @param sales_price                  : Selling price of the product
    */
  case class Transaction(
                          transaction_dt:Date,
                          customer_id:Int,
                          age_group:String,
                          pin_code:String,
                          product_subclass:Int,
                          product_id:Long,
                          amount:Int,
                          asset:Int,
                          sales_price:Int
                        )


  case class Demographics(
                           country:String,
                           population:Long
                         )
}

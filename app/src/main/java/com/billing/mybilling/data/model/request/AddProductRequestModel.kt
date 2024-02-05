package com.billing.mybilling.data.model.request


data class AddProductRequestModel(val category_id:String?, val product_name:String?=null,
                                  val isNonVeg: Int=0)
package com.billing.mybilling.data.model.request


data class AddVariantRequestModel(val pid:String?, val variant_name:String?=null,
                                  val price: String?=null)
package com.billing.mybilling.data.model.request

data class AddCategoryRequestModel(val id: String?, var name:String? = null,var image:String?=null,var imageUrl:String?=null,var isImageUpdate:Boolean = false)
package com.billing.mybilling.data.model.request

data class FCMSendRequest(val to:String,val notification:Map<String,String>,val data:Map<String,String>)
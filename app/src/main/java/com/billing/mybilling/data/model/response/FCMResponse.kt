package com.billing.mybilling.data.model.response

data class FCMResponse(val multicast_id:Long,val success:Int,val failure:Int,val cononical_ids:Int,val results:List<Result>)

data class Result(val message_id:String)
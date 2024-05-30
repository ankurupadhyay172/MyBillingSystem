package com.billing.mybilling.data.model.response

import com.billing.mybilling.utils.UserStatus
import com.billing.mybilling.utils.UserType

data class BusinessResponseModel(val status: Int,val result:Business)

data class Business(var business_id:String,
    var business_name:String?=null,var business_address:String?=null,var business_whats_app_no:String?=null,
                    var business_payment_qr_code:String?=null,val business_type:String,val business_currency:String,
                    var business_country:String,
    var active_status:Int)
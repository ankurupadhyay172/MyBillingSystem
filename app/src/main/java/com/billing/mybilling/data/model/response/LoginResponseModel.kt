package com.billing.mybilling.data.model.response

import com.billing.mybilling.utils.UserStatus
import com.billing.mybilling.utils.UserType

data class LoginResponseModel(val status: Int,val result:Users)

data class Users(val id:String?=null, var name:String, var user_id:String, var pass:String, val user_type:String,
                 val user_status:String=UserStatus.ACTIVE.status.toString(), var business_id:String, var user_type_id:Int=UserType.STAFF.id,
    var business_name:String?=null,var business_address:String?=null,var business_whats_app_no:String?=null)
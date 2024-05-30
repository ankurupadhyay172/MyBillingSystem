package com.billing.mybilling.data.model.request

import com.billing.mybilling.data.model.response.Table


data class AddOrderModel(val customer_name:String,val table_no:String?,
                        val customer_contact: String,val order_on: String?,
                         val order_user: String?,val id:String?,var table:Table?)
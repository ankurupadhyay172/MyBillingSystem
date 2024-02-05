package com.billing.mybilling.data.model.request

import com.billing.mybilling.data.model.response.Products

data class CartRequestModel(val id:String,val customer_name:String,val table_no:String,
                            val user_instruction:String,val customer_contact:String,
                            val order_on:String,val order_user:String,
                            val products: List<Products>)
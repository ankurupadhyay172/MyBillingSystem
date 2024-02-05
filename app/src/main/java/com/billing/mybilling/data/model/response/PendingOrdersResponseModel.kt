package com.billing.mybilling.data.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.billing.mybilling.utils.PaymentType
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class PendingOrdersResponseModel(
    @PrimaryKey(autoGenerate = false)
    val status: Int,val result:List<PendingOrders>){

    fun getOfflinePayment():Int{
        var tot = 0
        for (i in result){
            if (i.payment_type==PaymentType.OFFLINE.type){
                tot +=i.getTotalAmount()
            }
        }
        return tot
    }

    fun getOnlinePayment():Int {
            var tot = 0
            for (i in result){
                if (i.payment_type==PaymentType.ONLINE.type){
                    tot +=i.getTotalAmount()
                }
            }
           return tot
        }

    fun getTotalAmount():Int{
        var tot = 0
        for (i in result){
            tot +=i.getTotalAmount()
        }
        return tot
    }
}

@Entity
data class PendingOrders(@PrimaryKey(autoGenerate = false)
                         val order_id:String,val customer_name:String,val table_no:String,
                         val user_instruction: String,val customer_contact: String,
                         var payment_type:Int,var customer_discount:Int,var delivery_charges:Int,
                         val order_on:String,val timestamp:String,val order_user:String,var order_status:Int,
                         val timeLong:Long,val products: List<Products>?){

     fun getTotalAmount():Int{

            var tot = 0
            products?.let {
                for (p in it) {
                    tot += (p.price!!.toInt() * p.selected_quan)-customer_discount
                }
            }

        return tot
    }

}
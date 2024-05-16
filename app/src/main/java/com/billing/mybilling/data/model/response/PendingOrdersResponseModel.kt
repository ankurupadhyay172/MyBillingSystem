package com.billing.mybilling.data.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.billing.mybilling.utils.OrderStatus
import com.billing.mybilling.utils.OrderType
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
                         var order_id:String,val customer_name:String?=null,val table_no:String?=null,
                         var tableId:String?=null,
                         val user_instruction: String?=null,var customer_contact: String?=null,
                         var payment_type:Int=0,var customer_discount:Int=0,var delivery_charges:Int=0,
                         val order_on:String?=null,val timestamp:String?=null,val order_user:String?=null,
                         var order_status:Int, val timeLong:Long?=null,var products: List<Products>?=null,var receiveOnline:String="0",var receiveCash:String="0"){




     fun getTotalAmount():Int{

            var tot = 0
            if (order_status==OrderStatus.DELIVERED.status)
            products?.let {
                for (p in it) {
                    tot += (p.price!!.toInt() * p.selected_quan)-customer_discount
                }
            }

        return tot
    }

    fun formatDateToTimeAgo(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = dateFormat.parse(timestamp)
        val currentDate = Date()

        val diff = currentDate.time - date.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            days > 0 -> "$days days ago"
            hours > 0 -> "$hours hours ago"
            minutes > 0 -> "$minutes minutes ago"
            else -> "$seconds seconds ago"
        }
    }

}
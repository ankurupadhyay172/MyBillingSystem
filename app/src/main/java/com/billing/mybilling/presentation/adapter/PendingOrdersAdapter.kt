package com.billing.mybilling.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseListAdapter
import com.billing.mybilling.data.model.response.PendingOrders
import com.billing.mybilling.databinding.ItemPendingOrdersBinding
import com.billing.mybilling.utils.OrderStatus
import com.billing.mybilling.utils.OrderType
import com.billing.mybilling.utils.setOrderStatus
import javax.inject.Inject


class PendingOrdersAdapter @Inject constructor(): BaseListAdapter<PendingOrders,ItemPendingOrdersBinding>(DiffCallback()) {
    var open:((id:String?,order:PendingOrders?)->Unit)? = null
    var options:((id:String?,order:PendingOrders?)->Unit)? = null
    class DiffCallback: DiffUtil.ItemCallback<PendingOrders>(){
        override fun areItemsTheSame(oldItem: PendingOrders, newItem: PendingOrders): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: PendingOrders, newItem: PendingOrders): Boolean {
            return oldItem==newItem
        }

    }

    override fun createBinding(parent: ViewGroup): ItemPendingOrdersBinding {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_pending_orders,parent,false)
    }

    override fun bind(binding: ItemPendingOrdersBinding, item: PendingOrders?) {
        binding.userName.text = "Name : "+item?.customer_name
        binding.orderStatus.text = "Status : "+item?.order_status?.setOrderStatus()
//        binding.userDetail.text = item?.getOrderType()

        when(item?.order_on){
            OrderType.TABLE.type-> binding.liOrder.setBackgroundResource(R.color.appcolor)
            OrderType.PACKING.type-> binding.liOrder.setBackgroundResource(R.color.light_green)
            OrderType.ZOMATO.type-> binding.liOrder.setBackgroundResource(R.color.red)
            OrderType.SWIGGY.type-> binding.liOrder.setBackgroundResource(R.color.orange)
        }




            val orderType = when(item?.order_on){
                OrderType.TABLE.type->"Table No : ${item.table_no}"
                OrderType.PACKING.type -> "Packing"
                OrderType.ZOMATO.type -> "Zomato"
                OrderType.SWIGGY.type -> "Swiggy"
                else->""
            }
            binding.userDetail.text = orderType



        binding.price.text = item?.formatDateToTimeAgo()

        when(item?.order_status){
            OrderStatus.PENDING.status->binding.userImage.setImageResource(R.drawable.pendingorder)
            OrderStatus.COOKING.status->binding.userImage.setImageResource(R.drawable.cooking)
            OrderStatus.READY_TO_DELIVER.status->binding.userImage.setImageResource(R.drawable.readytosearve)
        }

        binding.liOrder.setOnClickListener {
           open?.invoke(item?.order_id,item)
       }
       binding.liOrder.setOnLongClickListener {
           options?.invoke(item?.order_id,item)
           return@setOnLongClickListener true
       }
    }
}
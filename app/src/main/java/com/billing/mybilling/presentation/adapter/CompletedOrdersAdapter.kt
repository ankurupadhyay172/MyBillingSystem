package com.billing.mybilling.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseListAdapter
import com.billing.mybilling.data.model.response.PendingOrders
import com.billing.mybilling.databinding.ItemCompletedOrdersBinding
import com.billing.mybilling.databinding.ItemPendingOrdersBinding
import com.billing.mybilling.utils.OrderStatus
import com.billing.mybilling.utils.setOrderStatus
import com.billing.mybilling.utils.setPrice
import javax.inject.Inject

class CompletedOrdersAdapter @Inject constructor(): BaseListAdapter<PendingOrders,ItemCompletedOrdersBinding>(DiffCallback()) {
    var open:((id:String?,order:PendingOrders?)->Unit)? = null

    class DiffCallback: DiffUtil.ItemCallback<PendingOrders>(){
        override fun areItemsTheSame(oldItem: PendingOrders, newItem: PendingOrders): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: PendingOrders, newItem: PendingOrders): Boolean {
            return oldItem==newItem
        }

    }

    override fun createBinding(parent: ViewGroup): ItemCompletedOrdersBinding {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_completed_orders,parent,false)
    }

    override fun bind(binding: ItemCompletedOrdersBinding, item: PendingOrders?) {
       binding.userName.text = item?.customer_name
        binding.price.text = item?.formatDateToTimeAgo()
        binding.userDetail.text = item?.getTotalAmount().toString().setPrice()
        binding.orderStatus.text = "Status : "+item?.order_status?.setOrderStatus()
       item?.order_status?.let {
           if (it==OrderStatus.FAILED.status){
               binding.userImage.setImageResource(R.drawable.cancel)
           }else{
               binding.userImage.setImageResource(R.drawable.check)
           }
       }

       binding.liOrder.setOnClickListener {
           open?.invoke(item?.order_id,item)
       }

    }
}
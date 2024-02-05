package com.billing.mybilling.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseListAdapter
import com.billing.mybilling.data.model.response.PendingOrders
import com.billing.mybilling.databinding.ItemPendingOrdersBinding
import javax.inject.Inject

class PendingOrdersAdapter @Inject constructor(): BaseListAdapter<PendingOrders,ItemPendingOrdersBinding>(DiffCallback()) {
    var open:((id:String?,order:PendingOrders?)->Unit)? = null

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
       binding.userName.text = "Customer Name : "+item?.customer_name
       binding.userDetail.text = "Table No : ${item?.table_no}"
       binding.liOrder.setOnClickListener {
           open?.invoke(item?.order_id,item)
       }
    }
}
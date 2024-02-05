package com.billing.mybilling.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.billing.mybilling.R
import com.billing.mybilling.data.model.response.PendingOrders
import com.billing.mybilling.databinding.ItemPendingOrdersBinding
import javax.inject.Inject
@ExperimentalPagingApi
class PendingOrdersPagingAdapter @Inject constructor(): PagingDataAdapter<PendingOrders,PendingOrdersPagingAdapter.MyViewModel>(DiffCallback()) {
    var open:((id:String?,order:PendingOrders?)->Unit)? = null
    class DiffCallback: DiffUtil.ItemCallback<PendingOrders>(){
        override fun areItemsTheSame(oldItem: PendingOrders, newItem: PendingOrders): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PendingOrders, newItem: PendingOrders): Boolean {
            return oldItem.order_id == newItem.order_id
        }

    }

    class MyViewModel(item:ItemPendingOrdersBinding): RecyclerView.ViewHolder(item.root){
        val binding = item
    }

    override fun onBindViewHolder(holder: MyViewModel, position: Int) {
        val item = getItem(position)
        holder.binding.userName.text = item?.customer_name
        holder.binding.userDetail.text = item?.table_no
        holder.binding.liOrder.setOnClickListener {
            open?.invoke(item?.order_id,item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewModel {
        return MyViewModel(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_pending_orders,parent,false))
    }
}
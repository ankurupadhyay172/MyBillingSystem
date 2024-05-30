package com.billing.mybilling.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseListAdapter
import com.billing.mybilling.data.model.response.PendingOrders
import com.billing.mybilling.databinding.ItemOrderTableBinding
import com.billing.mybilling.utils.OrderType
import javax.inject.Inject


class PendingTableOrdersAdapter @Inject constructor(): BaseListAdapter<PendingOrders,ItemOrderTableBinding>(DiffCallback()) {
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


    override fun createBinding(parent: ViewGroup): ItemOrderTableBinding {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_order_table,parent,false)
    }

    override fun bind(binding: ItemOrderTableBinding, item: PendingOrders?) {
//        binding.title.text = item?.table_no

        item?.let {
            if (item.order_on==OrderType.PACKING.type){
                binding.imgOrder.setImageResource(R.drawable.packing)
                binding.txtPacking.text = item.customer_name
                binding.mainLayout.setBackgroundResource(R.color.packingColor)
            }

            if (item.order_on==OrderType.TABLE.type){
                binding.imgOrder.setImageResource(R.drawable.table)
                binding.txtPacking.text = item.table_no
                binding.mainLayout.setBackgroundResource(R.color.appcolor)
            }

            if (item.order_on==OrderType.SWIGGY.type){
                binding.imgOrder.setImageResource(R.drawable.swiggy)
                binding.txtPacking.text = item.customer_name
                binding.mainLayout.setBackgroundResource(R.color.orange)
            }

            if (item.order_on==OrderType.ZOMATO.type){
                binding.imgOrder.setImageResource(R.drawable.zomato)
                binding.txtPacking.text = item.customer_name
                binding.mainLayout.setBackgroundResource(R.color.red)
            }
        }
        binding.mainLayout.setOnClickListener {
            open?.invoke(item?.order_id,item)
        }
    }

}
package com.billing.mybilling.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseListAdapter
import com.billing.mybilling.data.model.response.Products
import com.billing.mybilling.databinding.ItemOrderProductBinding
import javax.inject.Inject

class OrderItemAdapter @Inject constructor(): BaseListAdapter<Products,ItemOrderProductBinding>(DiffCallback()) {
    class DiffCallback: DiffUtil.ItemCallback<Products>(){
        override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem==newItem
        }
        override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem==newItem
        }
    }

    override fun bind(binding: ItemOrderProductBinding, item: Products?) {
        binding.model = item
        binding.tvNumber.text = "Quantity : ${item?.selected_quan}"
    }

    override fun createBinding(parent: ViewGroup): ItemOrderProductBinding {
       return DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_order_product,parent,false)
    }
}
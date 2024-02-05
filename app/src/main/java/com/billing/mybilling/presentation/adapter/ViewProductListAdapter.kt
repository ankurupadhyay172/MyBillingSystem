package com.billing.mybilling.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseListAdapter
import com.billing.mybilling.data.model.response.Products
import com.billing.mybilling.databinding.ItemProductsBinding
import com.billing.mybilling.databinding.ItemSearchProductsBinding
import com.billing.mybilling.utils.SelectedAction
import javax.inject.Inject

class ViewProductListAdapter @Inject constructor(): BaseListAdapter<Products,ItemProductsBinding>(DiffCallback()) {
    var open:((id: String?)->Unit)? = null
    var getData:((id: Products?,quan:Int)->Unit)? = null
    var option:((model:Products?)->Unit)? = null

    class DiffCallback: DiffUtil.ItemCallback<Products>(){
        override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem==newItem
        }

    }

    override fun createBinding(parent: ViewGroup): ItemProductsBinding {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_products,parent,false)
    }

    override fun bind(binding: ItemProductsBinding, item: Products?) {
        binding.tvTitle.text = item?.product_name
        binding.liProduct.setOnClickListener {
            open?.invoke(item?.pid)
        }

        binding.liProduct.setOnLongClickListener {
            option?.invoke(item)
            return@setOnLongClickListener true
        }

    }
}
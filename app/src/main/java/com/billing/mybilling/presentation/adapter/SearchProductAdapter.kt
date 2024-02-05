package com.billing.mybilling.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseListAdapter
import com.billing.mybilling.databinding.ItemSearchProductsBinding
import javax.inject.Inject

class SearchProductAdapter @Inject constructor(): BaseListAdapter<String,ItemSearchProductsBinding>(DiffCallback()) {

    class DiffCallback: DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    override fun createBinding(parent: ViewGroup): ItemSearchProductsBinding {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_search_products,parent,false)
    }

    override fun bind(binding: ItemSearchProductsBinding, item: String?) {
        binding.title.text = item
    }
}
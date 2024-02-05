package com.billing.mybilling.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseListAdapter
import com.billing.mybilling.data.model.response.Category
import com.billing.mybilling.databinding.ItemViewCategoryBinding
import javax.inject.Inject

class ViewCategoryAdapter @Inject constructor(): BaseListAdapter<Category,ItemViewCategoryBinding>(DiffCallback()) {
    var open:((id:String?,order: Category?)->Unit)? = null
    var option:((category:Category?)->Unit)? = null

    class DiffCallback: DiffUtil.ItemCallback<Category>(){
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem==newItem
        }

    }

    override fun createBinding(parent: ViewGroup): ItemViewCategoryBinding {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_view_category,parent,false)
    }

    override fun bind(binding: ItemViewCategoryBinding, item: Category?) {
       binding.tvTitle.text = item?.name
       binding.liOrder.setOnClickListener {
           open?.invoke(item?.category_id,item)
       }

       binding.liOrder.setOnLongClickListener {
           option?.invoke(item)
           return@setOnLongClickListener true
       }
    }
}
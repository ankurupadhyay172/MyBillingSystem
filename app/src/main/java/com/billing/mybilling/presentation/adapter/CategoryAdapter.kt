package com.billing.mybilling.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseListAdapter
import com.billing.mybilling.data.model.response.Category
import com.billing.mybilling.databinding.ItemCategoryBinding
import com.billing.mybilling.utils.setImage
import javax.inject.Inject

class CategoryAdapter @Inject constructor(): BaseListAdapter<Category,ItemCategoryBinding>(DiffCallback()) {
    var open:((id:String?)->Unit)? = null
    var option:((category:Category?)->Unit)? = null
    

    class DiffCallback: DiffUtil.ItemCallback<Category>(){
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem==newItem
        }

    }

    override fun createBinding(parent: ViewGroup): ItemCategoryBinding {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_category,parent,false)
    }

    override fun bind(binding: ItemCategoryBinding, item: Category?) {
        binding.name = item?.name
        binding.ivMain.setImage(item?.image)
        binding.liCategory.setOnClickListener {
            open?.invoke(item?.category_id)
        }

        binding.liCategory.setOnLongClickListener {
            option?.invoke(item)
            return@setOnLongClickListener true
        }
    }
}
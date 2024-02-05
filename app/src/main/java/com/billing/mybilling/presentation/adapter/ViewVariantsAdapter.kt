package com.billing.mybilling.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseListAdapter
import com.billing.mybilling.data.model.response.Category
import com.billing.mybilling.data.model.response.Variants
import com.billing.mybilling.databinding.ItemVariantsBinding
import com.billing.mybilling.databinding.ItemViewCategoryBinding
import com.billing.mybilling.utils.setPrice
import javax.inject.Inject

class ViewVariantsAdapter @Inject constructor(): BaseListAdapter<Variants,ItemVariantsBinding>(DiffCallback()) {
    var open:((id:String?,order: Variants?)->Unit)? = null
    var option:((model: Variants?)->Unit)? = null
    class DiffCallback: DiffUtil.ItemCallback<Variants>(){
        override fun areItemsTheSame(oldItem: Variants, newItem: Variants): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Variants, newItem: Variants): Boolean {
            return oldItem==newItem
        }

    }

    override fun createBinding(parent: ViewGroup): ItemVariantsBinding {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_variants,parent,false)
    }

    override fun bind(binding: ItemVariantsBinding, item: Variants?) {
        binding.userDetail.text = item?.price.toString().setPrice()
        binding.tvTitle.text = item?.variant_name
        binding.liProduct.setOnLongClickListener {
            option?.invoke(item)
            return@setOnLongClickListener true
        }

    }
}
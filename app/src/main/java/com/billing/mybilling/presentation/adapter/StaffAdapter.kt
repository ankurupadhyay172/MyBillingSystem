package com.billing.mybilling.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseListAdapter
import com.billing.mybilling.data.model.response.Category
import com.billing.mybilling.data.model.response.Users
import com.billing.mybilling.databinding.ItemCategoryBinding
import com.billing.mybilling.databinding.ItemUserBinding
import javax.inject.Inject

class StaffAdapter @Inject constructor(): BaseListAdapter<Users,ItemUserBinding>(DiffCallback()) {
    var open:((id:Users?)->Unit)? = null
    var options:((id:Users?)->Unit)? = null
    

    class DiffCallback: DiffUtil.ItemCallback<Users>(){
        override fun areItemsTheSame(oldItem: Users, newItem: Users): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Users, newItem: Users): Boolean {
            return oldItem==newItem
        }

    }

    override fun createBinding(parent: ViewGroup): ItemUserBinding {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_user,parent,false)
    }

    override fun bind(binding: ItemUserBinding, item: Users?) {
        binding.user = item
        binding.liUser.setOnClickListener {
            open?.invoke(item)
        }
        binding.liUser.setOnLongClickListener {
            options?.invoke(item)
            return@setOnLongClickListener true
        }

    }
}
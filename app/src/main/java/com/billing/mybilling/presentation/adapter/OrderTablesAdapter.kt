package com.billing.mybilling.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseListAdapter
import com.billing.mybilling.data.model.response.Category
import com.billing.mybilling.data.model.response.Table
import com.billing.mybilling.databinding.ItemCategoryBinding
import com.billing.mybilling.databinding.ItemTableBinding
import com.billing.mybilling.utils.setImage
import javax.inject.Inject

class OrderTablesAdapter @Inject constructor(): BaseListAdapter<Table,ItemTableBinding>(DiffCallback()) {
    var open:((id:Table?,pos:Int)->Unit)? = null
    var error:((msg:String?)->Unit)? = null
    

    class DiffCallback: DiffUtil.ItemCallback<Table>(){
        override fun areItemsTheSame(oldItem: Table, newItem: Table): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Table, newItem: Table): Boolean {
            return oldItem==newItem
        }

    }

    override fun createBinding(parent: ViewGroup): ItemTableBinding {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_table,parent,false)
    }


    override fun bind(binding: ItemTableBinding, item: Table?, position: Int) {

        binding.title.text = item?.table_name
        item?.let {
            if (it.isOccupied()) binding.mainLayout.setBackgroundResource(R.color.light_green)
            else if (it.isSelected) binding.mainLayout.setBackgroundResource(R.color.bluePrimary)
            else binding.mainLayout.setBackgroundResource(R.color.root_gray)
        }
        binding.mainLayout.setOnClickListener {
            item?.updateSelected()
            if (!item!!.isOccupied())
            open?.invoke(item,position)
            else
                error?.invoke("Table already booked")

        }
    }

}
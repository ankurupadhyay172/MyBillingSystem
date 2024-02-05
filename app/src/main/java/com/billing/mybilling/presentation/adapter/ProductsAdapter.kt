package com.billing.mybilling.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseListAdapter
import com.billing.mybilling.data.model.response.Products
import com.billing.mybilling.databinding.ItemSearchProductsBinding
import com.billing.mybilling.utils.SelectedAction
import javax.inject.Inject

class ProductsAdapter @Inject constructor(): BaseListAdapter<Products,ItemSearchProductsBinding>(DiffCallback()) {
    var open:((id: Products?,type:String)->Unit)? = null
    var getData:((id: Products?,quan:Int)->Unit)? = null

    class DiffCallback: DiffUtil.ItemCallback<Products>(){
        override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem==newItem
        }

    }

    override fun createBinding(parent: ViewGroup): ItemSearchProductsBinding {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_search_products,parent,false)
    }

    override fun bind(binding: ItemSearchProductsBinding, item: Products?) {
        binding.title.text = item?.product_name
        binding.counter = item?.selected_quan


        binding.addItemTxt.setOnClickListener {
            binding.counter = item?.incrementCounter()
            open?.invoke(item,SelectedAction.ADD.type)
            getData?.invoke(item,1)
            Log.d("TAG1", "onBindViewHolder: "+binding.counter)
        }

        binding.plusText.setOnClickListener {
            binding.counter =item?.incrementCounter()
            open?.invoke(item,SelectedAction.UPDATE.type)
            getData?.invoke(item,1)
            Log.d("TAG1", "onBindViewHolder: "+binding.counter)
        }
        binding.minusTxt.setOnClickListener {
            binding.counter =item?.decrementCounter()
            getData?.invoke(item,-1)
            if (binding.counter==0){
                open?.invoke(item,SelectedAction.DELETE.type)
            }else{
                open?.invoke(item,SelectedAction.UPDATE.type)
            }

        }
        binding.size.text = item?.variant_name
        binding.total.text = item?.price


    }
}
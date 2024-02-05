package com.billing.mybilling.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.billing.mybilling.data.model.response.Products
import com.billing.mybilling.databinding.ItemSearchProductsBinding
import javax.inject.Inject

class ProductsPagingAdapter @Inject constructor(): PagingDataAdapter<Products,ProductsPagingAdapter.MyViewHolder>(DiffUtilCallback()) {
    var open:((quan:Int,product: Products)->Unit)? =null
    class DiffUtilCallback: DiffUtil.ItemCallback<Products>() {
        override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem.selected_quan == oldItem.selected_quan
        }
    }

    class MyViewHolder(itemView: ItemSearchProductsBinding): RecyclerView.ViewHolder(itemView.root){
        val binding = itemView
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)!!
        val binding = holder.binding
        binding.title.text = item.product_name
        binding.counter = item.selected_quan
        Log.d("TAG1", "onBindViewHolder: "+binding.counter)
        binding.addItemTxt.setOnClickListener {

        binding.counter = item.incrementCounter()
            open?.invoke(item.selected_quan,item)
            Log.d("TAG1", "onBindViewHolder: "+binding.counter)
        }

        binding.plusText.setOnClickListener {
            binding.counter =item.incrementCounter()

            open?.invoke(item.selected_quan,item)
            Log.d("TAG1", "onBindViewHolder: "+binding.counter)
        }
        binding.minusTxt.setOnClickListener {
            binding.counter =item.decrementCounter()
            open?.invoke(item.selected_quan,item)
        }
        binding.size.text = item.variant_name
        binding.total.text = item.price

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       return MyViewHolder(ItemSearchProductsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
}
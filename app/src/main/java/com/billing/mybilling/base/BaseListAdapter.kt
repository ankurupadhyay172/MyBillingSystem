package com.billing.mybilling.base

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseListAdapter<T,V:ViewDataBinding>(diffCallback:DiffUtil.ItemCallback<T>):ListAdapter<T,BaseViewHolder<V>>(AsyncDifferConfig.Builder<T>(diffCallback).build()),AdapterBindingHelper<T,V> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<V> {
        val binding = createBinding(parent)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<V>, position: Int) {
        bind(holder.binding,getItem(position))
        bind(holder.binding,getItem(position),position)
        holder.binding.executePendingBindings()
    }

    abstract override fun createBinding(parent: ViewGroup): V

    override fun bind(binding: V, item: T?, position: Int) {

    }

}
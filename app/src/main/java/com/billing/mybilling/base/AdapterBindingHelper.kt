package com.billing.mybilling.base

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

interface AdapterBindingHelper<T,V: ViewDataBinding> {

    fun createBinding(parent:ViewGroup): V

    fun bind(binding: V,item: T?)

    fun bind(binding: V,item: T?,position: Int)
}
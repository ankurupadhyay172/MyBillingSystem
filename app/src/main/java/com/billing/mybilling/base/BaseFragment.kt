package com.billing.mybilling.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.billing.mybilling.utils.SingleLiveObserver
import com.billing.mybilling.utils.autoCleared

abstract class BaseFragment<T:ViewDataBinding,V: BaseViewModel>:Fragment() {
    private var mViewDataBinding by autoCleared<T>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<T>(inflater,getLayoutId(),container,false).also {
        mViewDataBinding = it
        mViewDataBinding.lifecycleOwner = viewLifecycleOwner
        mViewDataBinding.setVariable(getBindingVariable(),getViewModel())
        mViewDataBinding.executePendingBindings()
        initObservers()
    }.root

    private fun initObservers(){
        getViewModel().errorLiveData.observe(viewLifecycleOwner,SingleLiveObserver{appError->
            Toast.makeText(requireContext(), ""+appError, Toast.LENGTH_SHORT).show()
        })
    }

    fun getViewDataBinding(): T{
    return mViewDataBinding
    }

    abstract fun getLayoutId():Int
    abstract fun getBindingVariable():Int
    abstract fun getViewModel():V
}
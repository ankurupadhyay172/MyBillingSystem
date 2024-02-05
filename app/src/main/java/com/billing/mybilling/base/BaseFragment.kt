package com.billing.mybilling.base

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
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

    fun showToast(message:String)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun getViewDataBinding(): T{
    return mViewDataBinding
    }

    fun FragmentActivity.isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.activeNetwork
        } else {
            TODO("VERSION.SDK_INT < M")
        }
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }

    fun showLoading(visible:Boolean){
        if (visible)
            (requireActivity() as BaseActivity).showLoading(visible)
        else
            (requireActivity() as BaseActivity).hideLoading()
    }

    abstract fun getLayoutId():Int
    abstract fun getBindingVariable():Int
    abstract fun getViewModel():V
}
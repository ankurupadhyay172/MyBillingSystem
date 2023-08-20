package com.billing.mybilling.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.utils.SingleLiveDataEvent
import com.billing.mybilling.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment:BaseFragment<FragmentHomeBinding,HomeViewModel>() {
    private val homeViewModel:HomeViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewDataBinding().categories.setOnClickListener {
        homeViewModel.errorLiveData.postValue(SingleLiveDataEvent("data changed"))
        }
    }

    override fun getLayoutId() = R.layout.fragment_home
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
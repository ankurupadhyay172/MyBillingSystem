package com.billing.mybilling.presentation.about

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.databinding.FragmentAboutUsBinding
import com.billing.mybilling.presentation.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutUsFragment:BaseFragment<FragmentAboutUsBinding,HomeViewModel>() {
    val homeViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    override fun getLayoutId() = R.layout.fragment_about_us
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
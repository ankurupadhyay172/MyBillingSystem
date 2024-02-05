package com.billing.mybilling.presentation.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.LoginRequestModel
import com.billing.mybilling.utils.SingleLiveDataEvent
import com.billing.mybilling.databinding.FragmentHomeBinding
import com.billing.mybilling.databinding.FragmentLoginBinding
import com.billing.mybilling.databinding.FragmentSignupBinding
import com.billing.mybilling.presentation.HomeViewModel
import com.billing.mybilling.session.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment:BaseFragment<FragmentSignupBinding, LoginViewModel>() {
    private val homeViewModel:LoginViewModel by viewModels()
    @Inject
    lateinit var sessionManager: SessionManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getViewDataBinding().submit.setOnClickListener {

        }



    }

    override fun getLayoutId() = R.layout.fragment_signup
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
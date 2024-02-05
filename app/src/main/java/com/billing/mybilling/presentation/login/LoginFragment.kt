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
import com.billing.mybilling.presentation.HomeViewModel
import com.billing.mybilling.session.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment:BaseFragment<FragmentLoginBinding, LoginViewModel>() {
    private val homeViewModel:LoginViewModel by viewModels()
    @Inject
    lateinit var sessionManager: SessionManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getViewDataBinding().submit.setOnClickListener {
            showLoading(true)
            homeViewModel.loginUser(getViewDataBinding().mobileNo.text.toString()).observe(viewLifecycleOwner){
                it?.getInvalidInput()?.let {
                    showLoading(false)
                    showToast(it)
                }
                it.getErrorIfExists()?.let {
                    showLoading(false)
                    showToast(""+it.message)
                }
                it?.getValueOrNull()?.let {
                    showLoading(false)
                    if (it.status==1){
                        sessionManager.saveUser(it.result)
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                    }else{
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
                    }
                }
            }
        }



    }

    override fun getLayoutId() = R.layout.fragment_login
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
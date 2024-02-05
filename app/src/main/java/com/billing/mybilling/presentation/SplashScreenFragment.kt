package com.billing.mybilling.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.utils.SingleLiveDataEvent
import com.billing.mybilling.databinding.FragmentHomeBinding
import com.billing.mybilling.databinding.FragmentSplashScreenBinding
import com.billing.mybilling.session.SessionManager
import com.billing.mybilling.utils.UserType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenFragment:BaseFragment<FragmentSplashScreenBinding,HomeViewModel>() {
    private val homeViewModel:HomeViewModel by viewModels()
    @Inject
    lateinit var sessionManager: SessionManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.myLooper()!!).postDelayed({
            navigationToMain()

        },2000)
    }

    private fun navigationToMain(){
        if (sessionManager.getUser()==null){
            findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment())
        }else{
            sessionManager.getUser()?.let {
                when(it.user_type_id){
                    UserType.ADMIN.id -> findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment())
                    UserType.STAFF.id-> findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToPendingOrders())
                }
            }
        }
    }

    override fun getLayoutId() = R.layout.fragment_splash_screen
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
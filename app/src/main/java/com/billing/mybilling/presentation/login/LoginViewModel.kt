package com.billing.mybilling.presentation.login

import androidx.lifecycle.liveData
import com.billing.mybilling.base.BaseViewModel
import com.billing.mybilling.presentation.usecase.LoginUserCase
import com.billing.mybilling.utils.toLoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val loginUserCase: LoginUserCase):BaseViewModel(){

    fun loginUser(userId:String) = liveData(Dispatchers.IO){
            loginUserCase.loginUser(userId).toLoadingState().collect{
                emit(it)
            }
    }

}
package com.billing.mybilling.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.liveData
import com.billing.mybilling.data.model.request.LoginRequestModel
import com.billing.mybilling.data.model.response.LoginResponseModel
import com.billing.mybilling.data.repository.HomeRepository
import com.billing.mybilling.utils.LoadState
import com.billing.mybilling.utils.LoadingState
import com.billing.mybilling.utils.toLoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val homeRepository: HomeRepository) {
    suspend fun login(mobile:String,password:String):Flow<LoadState<LoginResponseModel>>{
        return if (mobile.isEmpty()||password.isEmpty()){
            flow { emit(LoadState.InvalidInput("Please enter valid userid and password")) }
        }else{
            homeRepository.loginUser(LoginRequestModel(mobile,password)).toLoadingState()
        }

    }
}
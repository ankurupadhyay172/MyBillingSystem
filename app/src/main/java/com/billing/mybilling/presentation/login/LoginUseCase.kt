package com.billing.mybilling.presentation.login


import com.billing.mybilling.data.model.request.LoginRequestModel
import com.billing.mybilling.data.model.response.LoginResponseModel
import com.billing.mybilling.data.repository.HomeRepository
import com.billing.mybilling.utils.LoadState
import com.billing.mybilling.utils.toLoadingState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
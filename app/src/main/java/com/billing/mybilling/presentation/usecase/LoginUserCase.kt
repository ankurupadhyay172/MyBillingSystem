package com.billing.mybilling.presentation.usecase

import com.billing.mybilling.data.model.request.LoginRequestModel
import com.billing.mybilling.data.model.response.LoginResponseModel
import com.billing.mybilling.presentation.login.LoginRepository
import com.billing.mybilling.utils.LoadState
import com.billing.mybilling.utils.toLoadingState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUserCase @Inject constructor(val repository: LoginRepository) {

    suspend fun loginUser(userId:String):Flow<LoginResponseModel>{
//        if (userId.isEmpty()||userId.length!=10){
//            emit(LoadState.InvalidInput("Please enter valid mobile number"))
//        }else
        return repository.loginUser(LoginRequestModel(userId))
    }
}
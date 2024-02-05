package com.billing.mybilling.presentation.login

import com.billing.mybilling.data.model.request.LoginRequestModel
import com.billing.mybilling.data.model.response.LoginResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(val loginApiService: LoginApiService):LoginRepository {
    override suspend fun loginUser(loginRequestModel: LoginRequestModel): Flow<LoginResponseModel> = flow{
        val response = loginApiService.loginUser(loginRequestModel)
        emit(response)
    }
}
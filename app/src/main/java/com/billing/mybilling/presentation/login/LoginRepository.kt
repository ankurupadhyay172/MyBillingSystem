package com.billing.mybilling.presentation.login

import com.billing.mybilling.data.model.request.LoginRequestModel
import com.billing.mybilling.data.model.response.LoginResponseModel
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun loginUser(loginRequestModel: LoginRequestModel): Flow<LoginResponseModel>
}
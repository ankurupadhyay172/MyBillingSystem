package com.billing.mybilling.presentation.login

import com.billing.mybilling.data.model.request.LoginRequestModel
import com.billing.mybilling.data.model.response.LoginResponseModel
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiService {

    @POST("LoginUser.php")
    suspend fun loginUser(@Body loginRequestModel: LoginRequestModel):LoginResponseModel
}
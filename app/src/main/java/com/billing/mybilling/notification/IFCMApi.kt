package com.billing.mybilling.notification


import com.billing.mybilling.data.model.request.FCMSendRequest
import com.billing.mybilling.data.model.response.FCMResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface IFCMApi {
    @Headers(
        "Content-Type: application/json", // Example header
        "Authorization:key=AAAAr8RmAwU:APA91bEVu5DH0AVzulNITpI2oZpAy7NZa5DRG7VA5MDp0ObJDEpc9Xr-Zbpk1tVr4CHe37Ap7d-8sx8eny4vA0KhgJMASA7I8G0bR5Rnuh6Fa-TSZebpiSr9NgFBMbJnTJkny5FeN8gF"
    )
    @POST("fcm/send")
    fun sendNotification(@Body body: FCMSendRequest?): Observable<FCMResponse>

}
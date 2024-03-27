package com.billing.mybilling.notification

import com.billing.mybilling.BuildConfig
import com.billing.mybilling.data.model.request.FCMSendRequest
import com.billing.mybilling.data.model.response.FCMResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

private val compositeDisposable by lazy { CompositeDisposable() }
fun sendNotificationToOrder(title:String,body:String,
                            callback:(response: FCMResponse)->Unit,errorCallback:(error:Throwable)->Unit){


    val ifcmApi = RetrofitClient.getInstance().create(IFCMApi::class.java)
    val notifyData = hashMapOf<String,String>()
    notifyData["title"] = title
    notifyData["body"] = body
    val data = hashMapOf<String,String>()
    data["title"] = title
    data["body"] = body
    val topic = BuildConfig.CHANNEL_ID

    val sendData =FCMSendRequest("/topics/$topic",notifyData,data)
    compositeDisposable.add(ifcmApi.sendNotification(sendData).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
        callback.invoke(it)

    },{
        errorCallback.invoke(it)
    }))
}
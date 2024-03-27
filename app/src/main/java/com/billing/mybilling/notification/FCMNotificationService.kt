package com.billing.mybilling.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.greenrobot.eventbus.EventBus

class FCMNotificationService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

    }
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        EventBus.getDefault().post(message)
        Log.d("mylog", "onMessageReceived: Notification received ${message.notification}")

    }
}
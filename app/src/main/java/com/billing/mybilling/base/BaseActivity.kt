package com.billing.mybilling.base

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.billing.mybilling.BuildConfig
import com.billing.mybilling.R
import com.billing.mybilling.session.SessionManager
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject

open class BaseActivity:AppCompatActivity() {
    private var progressDialog : ProgressDialog? = null
    @Inject
    lateinit var sessionManager1: SessionManager

    fun showLoading(visible:Boolean){
        if (visible){
            progressDialog = showLoadingDialog(this)
        }else{
            hideLoading()
        }
    }

    fun hideLoading() {
        progressDialog?.cancel()
    }

    private fun showLoadingDialog(context: Context): ProgressDialog {

        val progressDialog = ProgressDialog(context)
        progressDialog.show()
        if (progressDialog.window!=null){
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        progressDialog.setContentView(R.layout.spin_view)
        progressDialog.isIndeterminate = true
        //  progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        return progressDialog
    }

    fun setNotification(title:String?,body:String?,pendingIntent: PendingIntent?){
        val builder = NotificationCompat.Builder(this,getString(R.string.default_notification_channel_id)).apply {
            setSmallIcon(R.drawable.appicon)
            setContentTitle(title)
            setContentText(body)
            setTicker(getString(R.string.app_name))
            setAutoCancel(true)
            setContentInfo("Order Detail")
            pendingIntent?.let {
                setContentIntent(pendingIntent)
            }

        }

        val notification = builder.build()
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        try {
            val alarmSound = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://" + packageName + "/raw/notification"
            )
            val r = RingtoneManager.getRingtone(this, alarmSound)
            r.play()
            manager.notify(100, notification)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("myLogError", "setNotification: ${e.message}")
        }
    }
    fun unSubscribeToTopic(){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(sessionManager1.getUser()?.business_id+BuildConfig.CHANNEL_ID)
    }
}
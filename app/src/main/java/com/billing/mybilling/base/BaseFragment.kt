package com.billing.mybilling.base

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.billing.mybilling.BuildConfig
import com.billing.mybilling.session.SessionManager
import com.billing.mybilling.utils.SingleLiveObserver
import com.billing.mybilling.utils.autoCleared
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseFragment<T:ViewDataBinding,V: BaseViewModel>:Fragment() {
    private var mViewDataBinding by autoCleared<T>()
    @Inject
    lateinit var sessionManager1: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<T>(inflater,getLayoutId(),container,false).also {
        mViewDataBinding = it
        mViewDataBinding.lifecycleOwner = viewLifecycleOwner
        mViewDataBinding.setVariable(getBindingVariable(),getViewModel())
        mViewDataBinding.executePendingBindings()
        initObservers()
    }.root

    private fun initObservers(){
        getViewModel().errorLiveData.observe(viewLifecycleOwner,SingleLiveObserver{appError->
            Toast.makeText(requireContext(), ""+appError, Toast.LENGTH_SHORT).show()
        })
    }

    fun showToast(message:String)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun getViewDataBinding(): T{
    return mViewDataBinding
    }

    fun FragmentActivity.isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.activeNetwork
        } else {
            TODO("VERSION.SDK_INT < M")
        }
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }

    fun showLoading(visible:Boolean){
        if (visible)
            (requireActivity() as BaseActivity).showLoading(visible)
        else
            (requireActivity() as BaseActivity).hideLoading()
    }

    fun setNotification(title:String?,body:String?,pendingIntent: PendingIntent?){
        (requireActivity() as BaseActivity).setNotification(title,body,pendingIntent)
    }

    fun subscribeToOrderTopic(callback:(msg:String)->Unit){
        Log.d("subscribe21", "subscribeToOrderTopic: ${sessionManager1.getUser()?.business_id+BuildConfig.CHANNEL_ID}")
        FirebaseMessaging.getInstance().subscribeToTopic(sessionManager1.getUser()?.business_id+BuildConfig.CHANNEL_ID).addOnSuccessListener {
            callback.invoke("Success")
        }.addOnFailureListener {
            callback.invoke("${it.message}")
        }
    }

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // Permission is granted
                // You can perform the required action

                //Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                // Permission is denied
                // You can show a message or disable functionality that depends on the permission

            }
        }

    fun openGallery(galleryLauncher:ActivityResultLauncher<Intent>){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
            .addCategory(Intent.CATEGORY_OPENABLE)
            .setType("image/*")
        galleryLauncher.launch(intent)
    }
    fun requestImagePermission(callback: () -> Unit) {
        if (Build.VERSION.SDK_INT >= 33) {
            if (context?.checkCallingOrSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES)== PackageManager.PERMISSION_GRANTED){
                callback()
            }else
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)

        } else {
//            openGallery()
              callback.invoke()
        }
    }


    abstract fun getLayoutId():Int
    abstract fun getBindingVariable():Int
    abstract fun getViewModel():V
}
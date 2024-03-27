package com.billing.mybilling.test

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.data.remote.HomeApiService
import com.billing.mybilling.data.repository.HomeRepository
import com.billing.mybilling.session.SessionManager
import com.billing.mybilling.utils.toLoadingState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DataFetchService @Inject constructor(val homeRepository: HomeRepository):Service() {
    @Inject
    lateinit var sessionManager: SessionManager
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //fetchData()
        return START_STICKY
    }

//    private fun fetchData() {
//        CoroutineScope(Dispatchers.IO).launch {
//            homeRepository.getCategories(CommonRequestModel("3020331")).toLoadingState().collect{
//                Log.d("mylogresponse", "fetchData: ${it.getValueOrNull()}\n${it.getErrorIfExists()}")
//            }
//        }
//    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}
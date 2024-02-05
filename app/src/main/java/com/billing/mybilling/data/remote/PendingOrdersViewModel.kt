package com.billing.mybilling.data.remote

import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import com.billing.mybilling.base.BaseViewModel
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.data.repository.PendingOrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class PendingOrdersViewModel @Inject constructor(val repository: PendingOrderRepository):BaseViewModel() {

    fun getAllPendingOrders(commonRequestModel: CommonRequestModel) = liveData(Dispatchers.IO) {
        emit(repository.getAllPendingOrders(commonRequestModel))
    }
}
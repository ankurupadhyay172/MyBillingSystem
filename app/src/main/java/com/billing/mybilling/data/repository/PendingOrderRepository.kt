package com.billing.mybilling.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.data.remote.HomeApiService
import com.billing.mybilling.data.remote.PendingOrdersRemoteMediator
import com.billing.mybilling.database.DatabaseManager
import com.billing.mybilling.session.SessionManager
import javax.inject.Inject
@ExperimentalPagingApi
class PendingOrderRepository @Inject constructor(val homeApiService: HomeApiService, val databaseManager: DatabaseManager,
                            val sessionManager: SessionManager) {

    fun getAllPendingOrders(commonRequestModel: CommonRequestModel) = Pager(config = PagingConfig(pageSize = 40, maxSize = 120),
    remoteMediator = PendingOrdersRemoteMediator(commonRequestModel,homeApiService,databaseManager.getDatabase()),
    pagingSourceFactory = {databaseManager.getAllPendingOrders()}).liveData
}
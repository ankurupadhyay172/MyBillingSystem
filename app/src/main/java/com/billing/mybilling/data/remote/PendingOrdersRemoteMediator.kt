package com.billing.mybilling.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.data.model.response.PendingOrders
import com.billing.mybilling.database.MyDatabase

@OptIn(ExperimentalPagingApi::class)
class PendingOrdersRemoteMediator(
    val commonRequestModel: CommonRequestModel,
    val homeApiService: HomeApiService,
    val myDatabase: MyDatabase
): RemoteMediator<Int,PendingOrders>() {
    val myDao = myDatabase.getMyDataBaseDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PendingOrders>
    ): MediatorResult {

        val response = homeApiService.getPendingOrders(commonRequestModel)
        myDatabase.withTransaction {
            if (loadType==LoadType.REFRESH){
                myDao.clearPendingOrders()
            }
            if (response.status==1)
            myDao.addPendingOrders(response.result)
        }

        return MediatorResult.Success(endOfPaginationReached = true)
    }
}
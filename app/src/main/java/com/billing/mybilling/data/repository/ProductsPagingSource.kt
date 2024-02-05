package com.billing.mybilling.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.billing.mybilling.data.model.response.Products
import com.billing.mybilling.data.remote.HomeApiService

class ProductsPagingSource(val homeApiService: HomeApiService): PagingSource<Int, Products>() {
    override fun getRefreshKey(state: PagingState<Int, Products>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Products> {
        return try {
            val position = params.key?:1
            val response = homeApiService.getAllProductsByPagination(position)
            LoadResult.Page(
                data = response.result,
                prevKey = null,
                nextKey = position+1
            )
        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }
}
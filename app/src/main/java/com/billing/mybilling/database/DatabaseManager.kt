package com.billing.mybilling.database

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import com.billing.mybilling.data.model.ProductCart
import com.billing.mybilling.data.model.response.*
import kotlinx.coroutines.flow.Flow
import java.util.*

interface DatabaseManager {

    suspend fun tempAddToCart(cart: TempCart)
    fun getAllTempCart():List<TempCart>
    fun addCategory(list: List<Category>)
    fun addVariants(list: List<Variants>)
    fun addDelivered(list: List<PendingOrders>)
    fun getVariantsLive(id:String?):LiveData<List<Variants>>
    fun getCategoriesLive():LiveData<List<Category>>
    fun getAllOrdersByDate(id: Int,today:Date):LiveData<List<PendingOrders>>
    fun getAllOrdersByMonth(id: Int,today:Date):LiveData<List<PendingOrders>>
    fun getAllOrdersByYear(id: Int,today:Date):LiveData<List<PendingOrders>>
    fun addProducts(products:List<Products>)
    fun clearAllItems()
    fun getAllProductsLive(id: String?):LiveData<List<Products>>
    fun getAllProducts():List<Products>
    fun updateProduct(products: Products?)
    fun getProductsCart():List<ProductCart>
    fun getAllTheCartLive():LiveData<List<TempCart>>
    fun deleteTempCart(id: String)
    fun addPendingOrders(pendingOrders: List<PendingOrders>)
    fun getAllPendingOrders():PagingSource<Int,PendingOrders>
    fun clearPendingOrders()
    fun getDatabase(): MyDatabase
}
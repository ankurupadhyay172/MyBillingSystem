package com.billing.mybilling.database

import android.content.Context
import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Room
import com.billing.mybilling.data.model.ProductCart
import com.billing.mybilling.data.model.response.*
import com.billing.mybilling.utils.Common
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.temporal.TemporalAdjusters.lastDayOfMonth
import java.util.*
import javax.inject.Inject

class DatabaseManagerImpl @Inject constructor(@ApplicationContext context: Context) :
    DatabaseManager {

    val db = Room.databaseBuilder(context, MyDatabase::class.java, "myDatabase").build()
    override suspend fun tempAddToCart(cart: TempCart) {
        db.getMyDataBaseDao().addTempAddToCart(cart)
    }

    override fun getAllTempCart(): List<TempCart> {
        return db.getMyDataBaseDao().getAllTheCart()
    }

    override fun addCategory(list: List<Category>) {
        CoroutineScope(Dispatchers.IO).launch {
            db.getMyDataBaseDao().addCategory(list)
        }
    }

    override fun addVariants(list: List<Variants>) {
        CoroutineScope(Dispatchers.IO).launch {
            db.getMyDataBaseDao().addVariant(list)
        }
    }

    override fun addDelivered(list: List<PendingOrders>) {
        CoroutineScope(Dispatchers.IO).launch {
            db.getMyDataBaseDao().addDeliveredOrder(list)
        }
    }


    override fun addStaffAttendance(list: List<Attendance>) {
        CoroutineScope(Dispatchers.IO).launch {
            db.getMyDataBaseDao().addStaffAttendance(list)
        }
    }


    override fun getVariantsLive(id:String?): LiveData<List<Variants>> {
        return db.getMyDataBaseDao().getAllVariants(id)
    }

    override fun getCategoriesLive(): LiveData<List<Category>> {
        return db.getMyDataBaseDao().getAllCategoriesLive()
    }

    override fun getAllOrdersByDate(today:Date): LiveData<List<PendingOrders>> {
        return db.getMyDataBaseDao().getAllDeliveredOrdersByDate(today)
    }

    override fun getAllOrdersByMonth( today: Date): LiveData<List<PendingOrders>> {
        val format = SimpleDateFormat("yyyy").format(today)
        val start = Calendar.getInstance()
        start.set(format.toInt(),today.month,1)
        val end = Calendar.getInstance()
        val endDay = Common.lastDayOfMonth(format.toInt(),today.month+1)
        end.set(format.toInt(),today.month,endDay+1)
        return db.getMyDataBaseDao().getAllOrdersByMonth(start.time.time,end.time.time)
    }

    override fun getSingleStaffAttendanceByMonth( today: Date,id:String?): LiveData<List<Attendance>> {
        val format = SimpleDateFormat("yyyy").format(today)
        val start = Calendar.getInstance()
        start.set(format.toInt(),today.month,1)
        val end = Calendar.getInstance()
        val endDay = Common.lastDayOfMonth(format.toInt(),today.month+1)
        end.set(format.toInt(),today.month,endDay+1)
        return db.getMyDataBaseDao().getAllAttendanceByMonth(start.time.time,end.time.time,id)
    }

    override fun getAllOrdersByYear( today: Date): LiveData<List<PendingOrders>> {
        return db.getMyDataBaseDao().getAllOrdersByYear(today)
    }

    override fun getSingleStaffAttendanceByYear( today: Date): LiveData<List<Attendance>> {
        return db.getMyDataBaseDao().getAllAttendanceByYear(today)
    }


    override fun addProducts(products: List<Products>) {
        CoroutineScope(Dispatchers.IO).launch {
            db.getMyDataBaseDao().addProduct(products)
        }
    }

    override fun clearAllItems() {
        db.getMyDataBaseDao().deleteAllProducts()
    }

    override fun getAllProductsLive(id:String?): LiveData<List<Products>> {
        return db.getMyDataBaseDao().getProductsLive(id)
    }

    override fun getAllProducts(): List<Products> {
        return db.getMyDataBaseDao().getProducts()
    }

    override fun updateProduct(products: Products?) {
        CoroutineScope(Dispatchers.IO).launch {
            products?.let {
                with(db) { getMyDataBaseDao().updateProduct(product = products) }
            }
        }
    }

    override fun getProductsCart(): List<ProductCart> {
        return db.getMyDataBaseDao().getProductsCart()
    }

    override fun getAllTheCartLive(): LiveData<List<TempCart>> {
        return db.getMyDataBaseDao().getAllTheCartLive()
    }

    override fun deleteTempCart(id: String) {
        db.getMyDataBaseDao().deleteTempCart(id)
    }

    override fun addPendingOrders(pendingOrders: List<PendingOrders>) {
        CoroutineScope(Dispatchers.IO).launch {
            db.getMyDataBaseDao().addPendingOrders(pendingOrders)
        }
    }

    override fun getAllPendingOrders(): PagingSource<Int,PendingOrders> {
        return db.getMyDataBaseDao().getAllPendingOrdersFlow()
    }

    override fun clearPendingOrders() {
        CoroutineScope(Dispatchers.IO).launch {
            db.getMyDataBaseDao().clearPendingOrders()
        }
    }

    override fun getDatabase(): MyDatabase {
        return db
    }


}
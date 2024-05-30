package com.billing.mybilling.database

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.billing.mybilling.data.model.ProductCart
import com.billing.mybilling.data.model.response.*
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

@Dao
interface MyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTempAddToCart(tempCart: TempCart)

    @Query("select * from tempCart")
    fun getAllTheCart(): List<TempCart>

    @Query("select * from tempCart")
    fun getAllTheCartLive(): LiveData<List<TempCart>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduct(products:List<Products> )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategory(category: List<Category> )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDeliveredOrder(orders: List<PendingOrders>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStaffAttendance(attendance: List<Attendance>)

    @Query("select * from `pendingorders` where strftime('%d',datetime(timeLong/1000, 'unixepoch'))= strftime('%d',datetime(:today/1000, 'unixepoch'))  order by timeLong DESC")
    fun getAllDeliveredOrdersByDate(today: Date):LiveData<List<PendingOrders>>

    @Query("SELECT * FROM `pendingorders` where timeLong between :start and :end  order by timestamp DESC")
    fun getAllOrdersByMonth(start: Long,end: Long):LiveData<List<PendingOrders>>

    @Query("SELECT * FROM `pendingorders` WHERE datetime(datetime(timeLong/1000, 'unixepoch'),'start of year') = datetime(:today/1000,'unixepoch','start of year') order by timestamp DESC")
    fun getAllOrdersByYear(today:Date):LiveData<List<PendingOrders>>

    @Query("SELECT * FROM `attendance` where tapInTimeLong between :start and :end and user_id=:id order by tapInTimeLong DESC")
    fun getAllAttendanceByMonth(start: Long,end: Long,id: String?):LiveData<List<Attendance>>

    @Query("SELECT * FROM `attendance` WHERE datetime(datetime(tapInTimeLong/1000, 'unixepoch'),'start of year') = datetime(:today/1000,'unixepoch','start of year') order by tapInTimeLong DESC")
    fun getAllAttendanceByYear(today:Date):LiveData<List<Attendance>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVariant(variants: List<Variants>)

    @Query("select * from variants where pid=:id")
    fun getAllVariants(id:String?):LiveData<List<Variants>>

    @Query("select * from category")
    fun getAllCategoriesLive():LiveData<List<Category>>

    @Query("DELETE FROM Products")
    fun deleteAllProducts()

    @Query("select * from Products where category_id=:id")
    fun getProductsLive(id:String?): LiveData<List<Products>>

    @Query("select * from Products")
    fun getProducts(): List<Products>

    @Update
    fun updateProduct(product: Products)

    @Query("select * from Products left join TempCart on Products.vid=TempCart.id")
    fun getProductsCart():List<ProductCart>

    @Query("DELETE FROM TempCart WHERE id=:id")
    fun deleteTempCart(id:String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPendingOrders(pendingOrders: List<PendingOrders>)

    @Query("select * from PendingOrders")
    fun getAllPendingOrdersFlow():PagingSource<Int,PendingOrders>

    @Query("DELETE FROM PendingOrders")
    suspend fun clearPendingOrders()

}
package com.billing.mybilling.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.billing.mybilling.data.model.response.*

@Database(entities = [TempCart::class, Products::class,PendingOrders::class,Category::class,Variants::class,PendingOrdersResponseModel::class], version = 1, exportSchema = false)
@TypeConverters(MyConverter::class)
abstract class MyDatabase :RoomDatabase(){
    abstract fun getMyDataBaseDao(): MyDao
}
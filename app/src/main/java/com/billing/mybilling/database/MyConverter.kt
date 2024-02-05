package com.billing.mybilling.database

import androidx.room.TypeConverter
import com.billing.mybilling.data.model.response.PendingOrders
import com.billing.mybilling.data.model.response.PendingOrdersResponseModel
import com.billing.mybilling.data.model.response.Products
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class MyConverter {

    @TypeConverter
    fun fromTimestamp(value:Long?): Date?{
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?):Long?{
        return date?.time?.toLong()
    }

    @TypeConverter
    fun modelProductsToString(model: Products):String{
        val gson = Gson()
        val type = object : TypeToken<Products>(){}.type
        return gson.toJson(model,type)
    }

    @TypeConverter
    fun stringToModel(str: String): Products {
        val gson = Gson()
        val type = object :TypeToken<Products>(){}.type
        return gson.fromJson(str,type)
    }


    @TypeConverter
    fun modelProductsListToString(model: List<Products>?):String?{
        val gson = Gson()
        model?.let {
            val type = object : TypeToken<List<Products>>(){}.type
            return gson.toJson(model,type)
        }
        return null
    }

    @TypeConverter
    fun stringToProductList(str: String?): List<Products>? {
        val gson = Gson()
        str?.let {
            val type = object :TypeToken<List<Products>>(){}.type
            return gson.fromJson(str,type)
        }
        return null
    }


    @TypeConverter
    fun modelOrderToString(model: PendingOrdersResponseModel):String{
        val gson = Gson()
        val type = object : TypeToken<PendingOrdersResponseModel>(){}.type
        return gson.toJson(model,type)
    }

    @TypeConverter
    fun stringToOrder(str: String): PendingOrdersResponseModel {
        val gson = Gson()
        val type = object :TypeToken<PendingOrdersResponseModel>(){}.type
        return gson.fromJson(str,type)
    }


    @TypeConverter
    fun modelOrderListToString(result: List<PendingOrders>?):String?{
        val gson = Gson()
        result?.let {
            val type = object : TypeToken<List<PendingOrders>>(){}.type
            return gson.toJson(result,type)
        }
        return null
    }

    @TypeConverter
    fun stringToOrderList(str: String?): List<PendingOrders>? {
        val gson = Gson()
        str?.let {
            val type = object :TypeToken<List<PendingOrders>>(){}.type
            return gson.fromJson(str,type)
        }
        return null
    }


}
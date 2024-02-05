package com.billing.mybilling.data.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

data class ProductsResponseModel(val status: Int,val result: List<Products>)

@Entity
data class Products(
    @PrimaryKey(autoGenerate = false)
    @NotNull
    var pid: String,val id:Int,val category_id:String,val isNonVeg:Int,val product_name: String,val product_image: String,val timestamp: String,
    @Nullable
    val vid: String?,val variant_name: String?,val variant_image: String?,val description: String?,val price: String?,var selected_quan: Int,var product_order_id:String?,var business_id:String?){
    fun incrementCounter():Int{
        return ++selected_quan
    }
    fun decrementCounter():Int{
        return --selected_quan
    }

}
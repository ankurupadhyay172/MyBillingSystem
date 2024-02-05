package com.billing.mybilling.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.billing.mybilling.data.model.response.Products
import org.jetbrains.annotations.NotNull


@Entity
data class TempCart(
    @PrimaryKey(autoGenerate = false)
    @NotNull
    val id:String,
    val product: Products,
    val selectedQuantity:Int,
    val user:String="test"
)
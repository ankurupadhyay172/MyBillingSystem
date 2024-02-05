package com.billing.mybilling.data.model

import com.billing.mybilling.data.model.response.Products
import org.jetbrains.annotations.NotNull

data class ProductCart(
    @NotNull
    val vid:String, val product: Products, val selectedQuantity:Int)
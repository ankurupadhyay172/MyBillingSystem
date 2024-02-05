package com.billing.mybilling.data.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey

data class VariantsResponseModel(val status: Int,val result: List<Variants>)

@Entity
data class Variants(
    @PrimaryKey(autoGenerate = false)
    val vid: String,val pid:String,val variant_name: String,val variant_image: String,val description: String,var price: String)
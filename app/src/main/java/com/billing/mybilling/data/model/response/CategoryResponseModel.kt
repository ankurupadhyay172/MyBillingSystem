package com.billing.mybilling.data.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey

data class CategoryResponseModel(val status: Int,val result: List<Category>)

@Entity
data class Category(
    @PrimaryKey(autoGenerate = false)
    val category_id: String,val name: String,val image: String,val timestamp: String)
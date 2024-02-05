package com.billing.mybilling.utils

import android.util.Log

fun String.setPrice():String{
    return "₹ $this"
}

fun List<Int>.isAlready(){
    for (item in this){
        Log.d("mylog", "isAlready: $item")
    }
}
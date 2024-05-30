package com.billing.mybilling.utils

import androidx.annotation.IdRes
import com.billing.mybilling.R

enum class PageConfiguration(
    val id:Int,
    val toolbarVisible:Boolean = true
){
    ORDERS(R.id.deliveredOrdersFragment,false),
    SPLASH(R.id.splashScreenFragment,false),
    LOGIN(R.id.loginFragment,false),
    ANALYTICS(R.id.analyticsFragment,false),
    SINGLE_ATTENDANCE(R.id.singleAttendanceListFragment,false),
    OTHER(0);

    companion object{
        fun getConfiguration(@IdRes id:Int):PageConfiguration{
            return values().firstOrNull{it.id==id}?:OTHER
        }
    }
}
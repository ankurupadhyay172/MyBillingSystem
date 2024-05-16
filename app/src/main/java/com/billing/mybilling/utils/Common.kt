package com.billing.mybilling.utils

import java.time.LocalDate
import java.time.Year

class Common {

    companion object{
        fun lastDayOfMonth(Y: Int, M: Int): Int {
            return LocalDate.of(Y, M, 1).getMonth().length(Year.of(Y).isLeap)
        }


    }
}
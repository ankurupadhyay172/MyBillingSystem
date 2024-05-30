package com.billing.mybilling.data.model.response

import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.billing.mybilling.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity
data class Attendance(@PrimaryKey(autoGenerate = false)val id:String,val user_id:String,val attendance_mark:String,
    val tap_in_time:String,val tap_out_time:String,val business_id:String,val attendance_id:String,val attendance_date:String,
                      val tapInTimeLong:Long?=null,val tapOutTimeLong:Long?=null){


    fun findTotalWorkingHours(): String {
        if (tap_out_time != "0000-00-00 00:00:00") {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val tapInDate = dateFormat.parse(tap_in_time)
            val tapOutDate = dateFormat.parse(tap_out_time)

            val diffInMillies = tapOutDate.time - tapInDate.time
            val hours = diffInMillies / (1000 * 60 * 60)
            val minutes = (diffInMillies % (1000 * 60 * 60)) / (1000 * 60)
            return "$hours h $minutes min"
        } else {
            return ""
        }
    }
    fun formatTapInTimeAgo(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = dateFormat.parse(tap_in_time)


        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val formattedTime = timeFormat.format(date)
        val parts = formattedTime.split(" ")
        val time = parts[0] // Contains the time in hh:mm format
        val amPm = parts[1] // Contains either "AM" or "PM"
        return "$time $amPm"
    }


    fun formatTapOutTimeAgo(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = dateFormat.parse(tap_out_time)


        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val formattedTime = timeFormat.format(date)
        val parts = formattedTime.split(" ")
        val time = parts[0] // Contains the time in hh:mm format
        val amPm = parts[1] // Contains either "AM" or "PM"
        return "$time $amPm"
    }
}
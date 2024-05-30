package com.billing.mybilling.data.model.request





data class StaffAttendanceModel(val id:String?=null, var userId:String?, var attendanceMark:String,
                 val businessId:String?, var attendanceId:Int)
package com.billing.mybilling.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseListAdapter
import com.billing.mybilling.data.model.response.Attendance
import com.billing.mybilling.data.model.response.Category
import com.billing.mybilling.data.model.response.Users
import com.billing.mybilling.databinding.ItemAttendanceLogBinding
import com.billing.mybilling.databinding.ItemCategoryBinding
import com.billing.mybilling.databinding.ItemUserBinding
import com.billing.mybilling.utils.AttendanceType
import javax.inject.Inject

class StaffTimeLogAdapter @Inject constructor(): BaseListAdapter<Attendance,ItemAttendanceLogBinding>(DiffCallback()) {
    var open:((id:Users?)->Unit)? = null
    var options:((id:Users?)->Unit)? = null
    

    class DiffCallback: DiffUtil.ItemCallback<Attendance>(){
        override fun areItemsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
            return oldItem==newItem
        }

    }

    override fun createBinding(parent: ViewGroup): ItemAttendanceLogBinding {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_attendance_log,parent,false)
    }

    override fun bind(binding: ItemAttendanceLogBinding, item: Attendance?) {
        binding.userName.text = item?.attendance_date
        if(item?.attendance_id?.toInt()==AttendanceType.LEAVE.type){
            binding.hours.text = "On Leave"
            binding.tapIn.visibility = View.GONE
            binding.tapOut.visibility = View.GONE

        }else{
            binding.tapIn.visibility = View.VISIBLE
            binding.tapOut.visibility = View.VISIBLE
            binding.hours.text = item?.findTotalWorkingHours()
            binding.tapIn.text = item?.formatTapInTimeAgo()
            binding.tapOut.text = item?.formatTapOutTimeAgo()
        }

    }
}
package com.billing.mybilling.presentation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseListAdapter
import com.billing.mybilling.data.model.response.Users
import com.billing.mybilling.databinding.ItemUserAttendanceBinding
import com.billing.mybilling.utils.AttendanceType
import javax.inject.Inject

class StaffAttendanceItemAdapter @Inject constructor(): BaseListAdapter<Users,ItemUserAttendanceBinding>(DiffCallback()) {
    var tapIn:((id:Users?)->Unit)? = null
    var tapOut:((id:Users?)->Unit)? = null
    var leave:((id:Users?)->Unit)? = null
    var errorMsg:((id:String?)->Unit)? = null
    var callStaff:((id:String?)->Unit)? = null
    var open:((id:String?)->Unit)? = null
    class DiffCallback: DiffUtil.ItemCallback<Users>(){
        override fun areItemsTheSame(oldItem: Users, newItem: Users): Boolean {
            return oldItem==newItem
        }
        override fun areContentsTheSame(oldItem: Users, newItem: Users): Boolean {
            return oldItem==newItem
        }
    }

    override fun bind(binding: ItemUserAttendanceBinding, item: Users?) {
        binding.user = item
        item?.attendance?.let {
            if (it.attendance_id.toInt()!=AttendanceType.LEAVE.type){
                binding.tvTapIn.setTextColor(Color.WHITE)
                binding.tvTapIn.text = it.formatTapInTimeAgo()
                binding.liTapIn.backgroundTintList= ContextCompat.getColorStateList(binding.liTapIn.context, R.color.green)
                if (it.tap_out_time != "0000-00-00 00:00:00"){
                    binding.tvTapOut.setTextColor(Color.WHITE)
                    binding.tvTapOut.text = it.formatTapOutTimeAgo()
                    binding.liTapOut.backgroundTintList= ContextCompat.getColorStateList(binding.liTapIn.context, R.color.green)
                }

                binding.spendHours.text = it.findTotalWorkingHours()
            }else{
                binding.liLeave.backgroundTintList= ContextCompat.getColorStateList(binding.liTapIn.context, R.color.appcolor)
                binding.tvLeave.setTextColor(Color.WHITE)
            }



        }
        item?.let {
            val userType = if (it.user_type=="1") "Admin" else "Staff"
            binding.userName.text = "${item.name}(${userType})"
        }

        binding.liTapIn.setOnClickListener {
            if (item?.attendance==null)
            tapIn?.invoke(item)
            else
                errorMsg?.invoke("Already Tap In")
        }
        binding.liTapOut.setOnClickListener {
            if (item?.attendance!=null&&item.attendance.tap_out_time == "0000-00-00 00:00:00")
            tapOut?.invoke(item)
            else
                errorMsg?.invoke("Already Tap Out")
        }

        binding.liLeave.setOnClickListener {
            leave?.invoke(item)
        }
        binding.userContact.setOnClickListener {
            callStaff?.invoke(item?.user_id)
        }
        binding.cardView.setOnClickListener {
            open?.invoke(item?.user_id)
        }
    }

    override fun createBinding(parent: ViewGroup): ItemUserAttendanceBinding {
       return DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_user_attendance,parent,false)
    }
}
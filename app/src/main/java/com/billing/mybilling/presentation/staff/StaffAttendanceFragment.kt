package com.billing.mybilling.presentation.staff


import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.StaffAttendanceModel
import com.billing.mybilling.databinding.FragmentStaffAttendanceBinding
import com.billing.mybilling.presentation.HomeViewModel
import com.billing.mybilling.utils.AttendanceType
import com.billing.mybilling.utils.SelectedAction
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StaffAttendanceFragment:BaseFragment<FragmentStaffAttendanceBinding,HomeViewModel>() {
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getViewDataBinding().tapOut.setOnClickListener {
            manageStaffAttendance(SelectedAction.UPDATE.type)
        }

        getViewDataBinding().tapIn.setOnClickListener {
            manageStaffAttendance(SelectedAction.ADD.type,"present")
        }

        getViewDataBinding().liTapIn.setOnClickListener {
            manageStaffAttendance(SelectedAction.ADD.type)
        }

    }

    fun manageStaffAttendance(actionType:String,mark:String=""){
        showLoading(true)
        homeViewModel.manageStaffAttendance(actionType, StaffAttendanceModel(userId = homeViewModel.selectedUser?.user_id,
            attendanceMark = mark, attendanceId = AttendanceType.PRESENT.type, businessId = sessionManager1.getUser()?.business_id)).observe(viewLifecycleOwner){
            it.getValueOrNull()?.let {
                showLoading(false)
                showToast(""+it.result)
            }
        }
    }

    override fun getLayoutId() = R.layout.fragment_staff_attendance
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
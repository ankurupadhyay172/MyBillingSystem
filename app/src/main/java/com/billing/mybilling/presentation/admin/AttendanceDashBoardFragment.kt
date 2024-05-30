package com.billing.mybilling.presentation.admin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.data.model.request.StaffAttendanceModel
import com.billing.mybilling.data.model.response.Users
import com.billing.mybilling.databinding.FragmentAttendanceBinding
import com.billing.mybilling.presentation.HomeViewModel
import com.billing.mybilling.presentation.PendingOrdersDetailsFragmentDirections
import com.billing.mybilling.presentation.adapter.StaffAttendanceItemAdapter
import com.billing.mybilling.utils.AttendanceType
import com.billing.mybilling.utils.SelectedAction
import com.billing.mybilling.utils.showWarningDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AttendanceDashBoardFragment : BaseFragment<FragmentAttendanceBinding, HomeViewModel>() {
    val homeViewModel: HomeViewModel by viewModels()
    @Inject
    lateinit var adapter:StaffAttendanceItemAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewDataBinding().rvAttendance.adapter = adapter
        readStaffList()

        manageAdapterActions()
        getViewDataBinding().fabAdd.setOnClickListener {
        findNavController().navigate(AttendanceDashBoardFragmentDirections.actionAttendanceDashBoardFragmentToAddStaffFormFragment2())
        }

    }

    private fun manageAdapterActions() {
        adapter.tapIn = {it->
            createWarningDialogFunction("Are you sure Tap In ${it?.name}",SelectedAction.ADD.type,AttendanceType.PRESENT.type,it,"Present")

        }

        adapter.tapOut = {it->
            createWarningDialogFunction("Are you sure Tap Out ${it?.name}",SelectedAction.UPDATE.type,AttendanceType.ABSENT.type,it,"Tap Out")
        }

        adapter.leave = {
            createWarningDialogFunction("Are you sure mark Leave for ${it?.name}",SelectedAction.ADD.type,AttendanceType.LEAVE.type,it,"Leave")
        }

        adapter.open = {
            findNavController().navigate(AttendanceDashBoardFragmentDirections.actionAttendanceDashBoardFragmentToSingleAttendanceListFragment(it))
        }

        adapter.errorMsg = {
            showToast("$it")
        }
        adapter.callStaff = {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$it")
            // Check for permission before making the call
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(intent)
            } else {
                // Request permission from the user
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), 101)
            }
        }
    }

    private fun readStaffList() {
        showLoading(true)
        homeViewModel.readStaffAttendance(CommonRequestModel(sessionManager1.getUser()?.business_id)).observe(viewLifecycleOwner){
            it.getValueOrNull()?.let {
                showLoading(false)
                adapter.submitList(it.result)

                val presentList = it.result.filter { it.attendance?.attendance_id?.toInt()==AttendanceType.PRESENT.type }
                val leaveList = it.result.filter { it.attendance?.attendance_id?.toInt()==AttendanceType.LEAVE.type }
                val presentMembers = "${presentList.size}/${it.result.size}"
                getViewDataBinding().onlinePayment.text = presentMembers
                getViewDataBinding().cashPayment.text = leaveList.size.toString()

            }
        }
    }


    fun manageStaffAttendance(actionType:String,attendanceId:Int,mark:String,user:Users?){
        showLoading(true)
        homeViewModel.manageStaffAttendance(actionType, StaffAttendanceModel(userId = user?.user_id,
            attendanceMark = mark, attendanceId = attendanceId, businessId = sessionManager1.getUser()?.business_id)
        ).observe(viewLifecycleOwner){
            it.getValueOrNull()?.let {
                showLoading(false)
                showToast(""+it.result)

                readStaffList()
            }
        }
    }


    private fun createWarningDialogFunction(message: String, actionType: String, attendanceType: Int,user: Users?, status: String) {
            val dialog = showWarningDialog(
                requireActivity(),
                layoutInflater,
                message,
                {
                    manageStaffAttendance(actionType, attendanceType, status, user)
                },
                {
                    showLoading(false)
                })

            dialog.show()

    }


    override fun getLayoutId() = R.layout.fragment_attendance
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
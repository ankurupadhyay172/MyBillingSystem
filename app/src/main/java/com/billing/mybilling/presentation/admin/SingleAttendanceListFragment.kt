package com.billing.mybilling.presentation.admin

import android.Manifest
import android.app.AlertDialog

import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.data.model.request.StaffAttendanceModel
import com.billing.mybilling.data.model.response.Users
import com.billing.mybilling.database.DatabaseManager
import com.billing.mybilling.databinding.FragmentAttendanceBinding
import com.billing.mybilling.databinding.FragmentSingleAttendanceListBinding
import com.billing.mybilling.presentation.HomeViewModel
import com.billing.mybilling.presentation.adapter.StaffAttendanceItemAdapter
import com.billing.mybilling.presentation.adapter.StaffTimeLogAdapter
import com.billing.mybilling.utils.AnalyticsType
import com.billing.mybilling.utils.AttendanceType

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class SingleAttendanceListFragment : BaseFragment<FragmentSingleAttendanceListBinding, HomeViewModel>() {
    val homeViewModel: HomeViewModel by viewModels()
    @Inject
    lateinit var adapter:StaffTimeLogAdapter
    val args:SingleAttendanceListFragmentArgs by navArgs()
    @Inject
    lateinit var databaseManager: DatabaseManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleDateFunctionality()
        getViewDataBinding().rvAttendance.adapter = adapter
        homeViewModel.filterValue.value = AnalyticsType.MonthByMonth.type

        getViewDataBinding().imgFilter.setOnClickListener {
            implementFilter()
        }

        lifecycleScope.launch {
            homeViewModel.getSingleStaffAttendance(CommonRequestModel(args.id))
        }
        homeViewModel.analyticsType.observe(viewLifecycleOwner){
            setVisibility(it)

            databaseManager.getSingleStaffAttendanceByMonth(it,args.id).observe(viewLifecycleOwner){list->
                val calender = Calendar.getInstance()
                calender.time = it
                val maxDays = calender.getActualMaximum(Calendar.DATE)
                val presentList = list.filter { it.attendance_id.toInt()==AttendanceType.PRESENT.type }
                val leaveList = list.filter { it.attendance_id.toInt()==AttendanceType.LEAVE.type }
                adapter.submitList(list)

                getViewDataBinding().apply {
                    onlinePayment.text ="${presentList.size}"
                    cashPayment.text = "${leaveList.size}"
                    totalAmount.text = "${presentList.size} / $maxDays"
                }

            }
        }


    }


    private fun setVisibility(it: Date) {
        if (it >= Date()) {
            getViewDataBinding().rightArrow.visibility = View.GONE
        } else {
            getViewDataBinding().rightArrow.visibility = View.VISIBLE
        }
        getViewDataBinding().tvTitle.text = homeViewModel.setFilterData(it)
    }

    private fun implementFilter() {
        AlertDialog.Builder(requireContext()).setTitle(R.string.options)
            .setItems(R.array.attendanceFilterOptions) { dialog, which ->
                when (which) {
                    0, 1 -> {
                        val filterType = when (which) {
                            0 -> AnalyticsType.MonthByMonth.type
                            1 -> AnalyticsType.YearByYear.type
                            else -> throw IllegalArgumentException("Invalid filter position")
                        }
                        homeViewModel.filterValue.value = filterType
                        getViewDataBinding().apply {
                            homeViewModel.setFilterData(homeViewModel.analyticsType.value!!)
                            homeViewModel.analyticsType.value =
                                homeViewModel.analyticsType.value
                        }
                    }

                }


            }.show()

    }

    private fun handleDateFunctionality() {
        getViewDataBinding().leftArrow.setOnClickListener {
            homeViewModel.decrementCounter()
        }

        getViewDataBinding().rightArrow.setOnClickListener {
            homeViewModel.incrementCounter()
        }
        getViewDataBinding().imgBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun getLayoutId() = R.layout.fragment_single_attendance_list
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
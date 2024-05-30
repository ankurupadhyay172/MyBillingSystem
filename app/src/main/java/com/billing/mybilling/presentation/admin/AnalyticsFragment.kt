package com.billing.mybilling.presentation.admin

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.AddCategoryRequestModel
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.data.model.response.Category
import com.billing.mybilling.database.DatabaseManager
import com.billing.mybilling.databinding.FragmentAddCategoryFormBinding
import com.billing.mybilling.databinding.FragmentAddDiscountFormBinding
import com.billing.mybilling.databinding.FragmentAnalyticsBinding
import com.billing.mybilling.databinding.FragmentQrCodeBinding
import com.billing.mybilling.presentation.HomeViewModel
import com.billing.mybilling.session.SessionManager
import com.billing.mybilling.utils.AnalyticsType
import com.billing.mybilling.utils.OrderStatus
import com.billing.mybilling.utils.SelectedAction
import com.billing.mybilling.utils.editType
import com.billing.mybilling.utils.setImage
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class AnalyticsFragment : BaseFragment<FragmentAnalyticsBinding, HomeViewModel>() {
    val homeViewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var databaseManager: DatabaseManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            homeViewModel.getCompletedOrders(CommonRequestModel(sessionManager1.getUser()?.business_id,OrderStatus.DELIVERED.status.toString()))
        }
        homeViewModel.analyticsType.observe(viewLifecycleOwner) {
            setVisibility(it)
            val filterFunction = when (homeViewModel.filterValue.value) {
                AnalyticsType.DayByDay.type -> databaseManager::getAllOrdersByDate
                AnalyticsType.MonthByMonth.type -> databaseManager::getAllOrdersByMonth
                AnalyticsType.YearByYear.type -> databaseManager::getAllOrdersByYear
                else -> throw IllegalArgumentException("Invalid filter type")
            }

            filterFunction(it).observe(viewLifecycleOwner) { orders ->
                val frequencyMap = mutableMapOf<String, Int>()
                if (orders.isNullOrEmpty()){
                    getViewDataBinding().pieChart.visibility = View.GONE
                    getViewDataBinding().tvPieChart.visibility = View.GONE
                }else{
                    getViewDataBinding().pieChart.visibility = View.VISIBLE
                    getViewDataBinding().tvPieChart.visibility = View.VISIBLE
                }
                orders.forEach { order ->
                    order.products?.forEach { product ->
                        if (order.order_status == OrderStatus.DELIVERED.status) {
                            val key = product.product_name
                            val value = frequencyMap[key] ?: 0
                            frequencyMap[key] = value + product.selected_quan

                        }
                    }
                }
                addPieChart(frequencyMap)
                addBarChart()
            }
        }
        handleDateFunctionality()
    }

    private fun addBarChart() {
        val date = homeViewModel.instance
        val month = date.get(Calendar.MONTH)
        val calendar = Calendar.getInstance()
        val year = if (calendar.get(Calendar.YEAR) == date.get(Calendar.YEAR)) {
            month
        } else {
            11
        }
        val entries = mutableListOf<BarEntry>()
        val frequencyMap = mutableMapOf<Int, Int>()
        val expectedObservations = year + 1 // Assuming year is the last month index
        var observationCounter = 0
        for (i in 0..year) {
            date.set(Calendar.MONTH, i)
            databaseManager.getAllOrdersByMonth(date.time).observe(viewLifecycleOwner) {
                val totalSum = it.sumOf { it.getTotalAmount() }
                frequencyMap[i] = totalSum
                observationCounter++
                entries.add(BarEntry(i.toFloat(), totalSum.toFloat()))
                if (observationCounter == expectedObservations) {
                    Log.d("mySalesChart", "addBarChart: $frequencyMap")
                    val barDataSet = BarDataSet(entries, "")
                    val barData = BarData(barDataSet)
                    barDataSet.setColors(ColorTemplate.MATERIAL_COLORS, 900)
                    getViewDataBinding().barChart.apply {
                        this.data = barData
                        legend.isEnabled = false
                        setFitBars(true)
                        description.isEnabled = false
                        axisRight.setDrawLabels(false)
                        invalidate()
                        val xAxis = getViewDataBinding().barChart.xAxis
                        xAxis.labelCount = 1
                        xAxis.setDrawGridLines(false)
                        xAxis.granularity = 1f
                        xAxis.isGranularityEnabled = true
                        xAxis.labelCount = year
                        xAxis.setAvoidFirstLastClipping(true)
                        xAxis.position = XAxis.XAxisPosition.BOTTOM
                        barData.setValueFormatter(object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return "₹ %.0f".format(value)
                            }
                        })
                        xAxis.valueFormatter = object : IndexAxisValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return homeViewModel.yearName[value.toInt()]
                            }
                        }
                    }
                }

            }


        }

        val xAxis = getViewDataBinding().barChart.xAxis
        xAxis.labelCount = 1
        xAxis.position = XAxis.XAxisPosition.BOTTOM
    }

    private fun addPieChart(frequencyMap: MutableMap<String, Int>) {

        val pieEntries = ArrayList<PieEntry>()
        frequencyMap.forEach {
            pieEntries.add(PieEntry(it.value.toFloat(), it.key))
        }
        val dataSet = PieDataSet(pieEntries, "Items")
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS, 1000)
        val data = PieData(dataSet)

        data.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "%.0f".format(value)
            }
        })
        dataSet.valueTextSize = 12f
        getViewDataBinding().pieChart.setEntryLabelTextSize(9f)
        getViewDataBinding().pieChart.data = data
        getViewDataBinding().pieChart.invalidate()
        val description = Description()
        description.text = ""
        getViewDataBinding().pieChart.description = description
        getViewDataBinding().pieChart.legend.isWordWrapEnabled = true
        getViewDataBinding().pieChart.legend.maxSizePercent = 1f
//        getViewDataBinding().pieChart.legend.isEnabled = false

    }

    private fun setVisibility(it: Date) {
        if (it >= Date()) {
            getViewDataBinding().rightArrow.visibility = View.GONE
        } else {
            getViewDataBinding().rightArrow.visibility = View.VISIBLE
        }
        getViewDataBinding().tvTitle.text = homeViewModel.setFilterData(it)
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
        getViewDataBinding().imgFilter.setOnClickListener {
            implementFilter()
        }
    }

    private fun implementFilter() {
        AlertDialog.Builder(requireContext()).setTitle(R.string.options)
            .setItems(R.array.saleFilterOptions) { dialog, which ->
                when (which) {
                    0, 1, 2 -> {
                        val filterType = when (which) {
                            0 -> AnalyticsType.DayByDay.type
                            1 -> AnalyticsType.MonthByMonth.type
                            2 -> AnalyticsType.YearByYear.type
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


    override fun getLayoutId() = R.layout.fragment_analytics
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
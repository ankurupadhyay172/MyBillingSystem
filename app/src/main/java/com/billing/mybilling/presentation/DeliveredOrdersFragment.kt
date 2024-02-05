package com.billing.mybilling.presentation

import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.database.DatabaseManager
import com.billing.mybilling.databinding.FragmentDeliveredOrdersBinding
import com.billing.mybilling.presentation.adapter.CompletedOrdersAdapter
import com.billing.mybilling.session.SessionManager
import com.billing.mybilling.utils.AnalyticsType
import com.billing.mybilling.utils.OrderStatus
import com.billing.mybilling.utils.PaymentType
import com.billing.mybilling.utils.setPrice
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DeliveredOrdersFragment : BaseFragment<FragmentDeliveredOrdersBinding, HomeViewModel>() {
    private val homeViewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var adapter: CompletedOrdersAdapter

    @Inject
    lateinit var databaseManager: DatabaseManager
    @Inject
    lateinit var sessionManager: SessionManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spinnerAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.filter_options,
            android.R.layout.simple_spinner_dropdown_item
        )
        getViewDataBinding().apply {
            rvMangeAddress.adapter = adapter
            orderFilter.adapter = spinnerAdapter
        }

        lifecycleScope.launch {
            homeViewModel.getCompletedOrders(CommonRequestModel(sessionManager.getUser()?.business_id,OrderStatus.DELIVERED.status.toString()))
        }


        setFilter()
        handleIncrement()


//        homeViewModel.getCompletedOrders(CommonRequestModel(OrderStatus.DELIVERED.status.toString())).observe(viewLifecycleOwner){
//            it.getValueOrNull()?.let {
//                    getViewDataBinding().progressBar.visibility = View.GONE
//                    getViewDataBinding().totalAmount.text = it.getTotalAmount().toString()
//                    adapter.submitList(it.result)
//
//                    getViewDataBinding().onlinePayment.text = "Online Payment : ${it.getOnlinePayment().toString().setPrice()}"
//
//                    getViewDataBinding().cashPayment.text = "Offline Payment : ${it.getOfflinePayment().toString().setPrice()}"
//            }
//        }


    }

    private fun handleIncrement() {
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

    private fun setFilter() {
        homeViewModel.analyticsType.observe(viewLifecycleOwner) {
            val filterFunction = when (homeViewModel.filterValue.value) {
                AnalyticsType.DayByDay.type -> databaseManager::getAllOrdersByDate
                AnalyticsType.MonthByMonth.type -> databaseManager::getAllOrdersByMonth
                AnalyticsType.YearByYear.type -> databaseManager::getAllOrdersByYear
                else -> throw IllegalArgumentException("Invalid filter type")
            }
            val c = Calendar.getInstance().time

            getViewDataBinding().rightArrow.visibility = if (it.date == c.date && it.month == c.month && it.year == c.year) View.GONE else View.VISIBLE

            filterFunction(OrderStatus.DELIVERED.status, it).observe(viewLifecycleOwner) {
                getViewDataBinding().isEmpty = it.isEmpty()
                adapter.submitList(it)
                getViewDataBinding().apply {
                    progressBar.visibility = View.GONE
                    totalAmount.text = it.sumOf { it.getTotalAmount() }.toString()
                    onlinePayment.text = "Online Payment : ${it.filter { it.payment_type== PaymentType.ONLINE.type }.sumOf { it.getTotalAmount() }.toString().setPrice()}"
                    cashPayment.text = "Cash Payment : ${it.filter { it.payment_type== PaymentType.OFFLINE.type }.sumOf { it.getTotalAmount() }.toString().setPrice()}"
                }
            }
        }

        getViewDataBinding().orderFilter.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    when (pos) {
                        0, 1, 2 -> {
                            val filterType = when (pos) {
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

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    homeViewModel.filterValue.value = AnalyticsType.DayByDay.type
                }

            }
        homeViewModel.analyticsType.observe(viewLifecycleOwner) {
            getViewDataBinding().tvTitle.text = homeViewModel.setFilterData(it)

        }


    }

    override fun getLayoutId() = R.layout.fragment_delivered_orders
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
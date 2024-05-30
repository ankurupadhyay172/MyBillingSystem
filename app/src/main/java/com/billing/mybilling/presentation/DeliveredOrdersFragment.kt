package com.billing.mybilling.presentation

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.data.model.response.Products
import com.billing.mybilling.database.DatabaseManager
import com.billing.mybilling.databinding.FragmentDeliveredOrdersBinding
import com.billing.mybilling.presentation.adapter.CompletedOrdersAdapter
import com.billing.mybilling.session.SessionManager
import com.billing.mybilling.utils.AnalyticsType
import com.billing.mybilling.utils.OrderStatus
import com.billing.mybilling.utils.OrderType
import com.billing.mybilling.utils.setPrice
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
import java.lang.NumberFormatException
import java.text.SimpleDateFormat
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
        adapter.open = {id,item->
              findNavController().navigate(DeliveredOrdersFragmentDirections.actionDeliveredOrdersFragmentToOrderDetailFragment(Gson().toJson(item)))
        }

        adapter.contactPerson =  {phoneNumber->
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phoneNumber")
            // Check for permission before making the call
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(intent)
            } else {
                // Request permission from the user
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), 101)
            }
        }

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

            filterFunction( it).observe(viewLifecycleOwner) {orders->
                val frequencyMap = mutableMapOf<String, Int>()

                var myTotalAmount = 0
                var myOnlineAmount = 0
                var myCashPayment = 0
                var myZomatoPayment = 0
                var mySwiggyPayment = 0

                orders.forEach {order->
                    myTotalAmount += order.getTotalAmount()
                    try {
                        if (order.order_status==OrderStatus.DELIVERED.status){
                            myOnlineAmount += order.receiveOnline.toInt()
                            myCashPayment += order.receiveCash.toInt()
                        }
                        if (order.order_on==OrderType.ZOMATO.type){
                            myZomatoPayment += order.getTotalAmount()
                        }
                        if (order.order_on==OrderType.SWIGGY.type){
                            mySwiggyPayment += order.getTotalAmount()
                        }

                    }catch (e:NumberFormatException){
                        Log.d("myLogException", "setFilter: ${e.message}")
                    }

                    order.products?.forEach {product->

                        // Calculate frequency of each product
                        if (order.order_status==OrderStatus.DELIVERED.status){
                            val key = product.product_name
                            val value = frequencyMap[key] ?: 0
                            frequencyMap[key] = value + product.selected_quan

                        }

                    }

                }
                getViewDataBinding().apply {
                    isEmpty = orders.isEmpty()
                    adapter.submitList(orders)
                    progressBar.visibility = View.GONE
                    totalAmount.text = myTotalAmount.toString()
                    onlinePayment.text = "Online : ${myOnlineAmount.toString().setPrice()}\nCash : ${myCashPayment.toString().setPrice()}"
                    cashPayment.text = "Swiggy : ${mySwiggyPayment.toString().setPrice()}\nZomato : ${myZomatoPayment.toString().setPrice()}"
                }

                Log.d("myLogProducts", "setFilter: $frequencyMap")

            }
        }

        getViewDataBinding().imgFilter.setOnClickListener {
            implementFilter()
        }


        homeViewModel.analyticsType.observe(viewLifecycleOwner) {
            getViewDataBinding().tvTitle.text = homeViewModel.setFilterData(it)

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

    override fun getLayoutId() = R.layout.fragment_delivered_orders
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
package com.billing.mybilling.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.AddOrderModel
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.data.model.response.PendingOrders
import com.billing.mybilling.data.model.response.Table
import com.billing.mybilling.databinding.FragmentAddOrderBinding
import com.billing.mybilling.notification.sendNotificationToOrder
import com.billing.mybilling.presentation.adapter.OrderTablesAdapter
import com.billing.mybilling.session.SessionManager
import com.billing.mybilling.utils.OrderStatus
import com.billing.mybilling.utils.OrderType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddOrderFragment: BaseFragment<FragmentAddOrderBinding,HomeViewModel>() {
    val homeViewModel: HomeViewModel by activityViewModels()
    @Inject
    lateinit var sessionManager: SessionManager
    @Inject
    lateinit var adapter: OrderTablesAdapter
    var list:List<Table>? = emptyList()
    var table:Table? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewDataBinding().isTable = true
        getViewDataBinding().fragment = this
        getViewDataBinding().rvTable.adapter = adapter
//        val table  = if (getViewDataBinding().isTable!!) OrderType.TABLE.type else OrderType.PACKING.type
        homeViewModel.readTable(CommonRequestModel(sessionManager.getUser()?.business_id)).observe(viewLifecycleOwner){
            it.getValueOrNull()?.let {
                list = it.result
                adapter.submitList(list)
            }
        }


        getViewDataBinding().checkout.setOnClickListener {
            if (table == null && getViewDataBinding().isTable!!) {
                showToast("Please Select Order Table ")
            } else {
                val isTable =
                    if (getViewDataBinding().isTable!!) OrderType.TABLE.type else OrderType.PACKING.type
                val name = getViewDataBinding().edtCustomerName.text.toString()
                val mobile = getViewDataBinding().edtCustomerMobile.text.toString()
                showLoading(true)

                homeViewModel.createMyOrder(
                    AddOrderModel(
                        name,
                        table?.table_name,
                        mobile,
                        isTable,
                        sessionManager.getUser()?.user_id,
                        sessionManager.getUser()?.business_id,
                        table
                    )
                ).observe(viewLifecycleOwner) {
                    it.getValueOrNull()?.let {
                        showLoading(false)
                        if (it.status == 1) {
                            if (getViewDataBinding().isTable!!) {
                                sendNotificationToOrder("New Order Found ",
                                    "On Table No : ${table?.table_name}",
                                    {

                                    },
                                    {

                                    })
                            } else {
                                sendNotificationToOrder("New Order Found ", "For packing",
                                    {

                                    }, {

                                    })
                            }
                            homeViewModel.pendingOrders = it.order
                            findNavController().navigate(AddOrderFragmentDirections.actionAddOrderFragment2ToCategoryFragment())
                        }
                    }

                }
            }
        }

        adapter.open = {item,pos->
            list?.onEach { it.isSelected = false }
            list!![pos].isSelected = true
            adapter.notifyDataSetChanged()
            table = item
        }
        adapter.error = {
            showToast(""+it)
        }

    }
    fun onTableClick(){
        getViewDataBinding().isTable = true
    }
    fun onPackingClick(){
        list?.onEach { it.isSelected = false }
        adapter.notifyDataSetChanged()
        table = null
        getViewDataBinding().isTable = false
    }


    override fun getLayoutId() = R.layout.fragment_add_order
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
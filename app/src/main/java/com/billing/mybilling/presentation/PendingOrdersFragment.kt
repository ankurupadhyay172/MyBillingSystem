package com.billing.mybilling.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.data.model.response.PendingOrders
import com.billing.mybilling.databinding.FragmentPendingOrdersBinding
import com.billing.mybilling.notification.sendNotificationToOrder
import com.billing.mybilling.presentation.adapter.PendingOrdersAdapter
import com.billing.mybilling.session.SessionManager
import com.billing.mybilling.utils.OrderStatus
import com.billing.mybilling.utils.SelectedAction
import com.billing.mybilling.utils.setOrderStatus
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class PendingOrdersFragment: BaseFragment<FragmentPendingOrdersBinding,HomeViewModel>() {
    val homeViewModel: HomeViewModel by activityViewModels()

    @Inject
    lateinit var adapter: PendingOrdersAdapter
    @Inject
    lateinit var sessionManager: SessionManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewDataBinding().rvOrders.adapter = adapter

        showLoading(true)
        readOrders()

        getViewDataBinding().swipeToRefresh.setOnRefreshListener {
            readOrders()
        }

        adapter.open = {id,item->
            homeViewModel.pendingOrders = item
            findNavController().navigate(PendingOrdersFragmentDirections.actionPendingOrdersToPendingOrdersDetailsFragment(id!!,"Table No : "+item?.table_no))
        }

        getViewDataBinding().fabAdd.setOnClickListener {
            findNavController().navigate(PendingOrdersFragmentDirections.actionPendingOrdersToAddOrderFragment2())
        }

        adapter.options = {id,item->
            AlertDialog.Builder(requireContext()).setTitle(R.string.option_order)
                .setItems(R.array.options_orders) { dialog, which ->
                    showLoading(true)
                homeViewModel.updatePendingOrder(SelectedAction.UPDATE_STATUS.type, PendingOrders(order_id = id!!, order_status = which)).observe(viewLifecycleOwner){
                    it.getErrorIfExists()?.let {
                        showLoading(false)
                        showToast("${it.message}")
                    }
                    it.getValueOrNull()?.let {
                     showLoading(false)
                     readOrders()
                     sendNotificationToOrder("Order Updated",which.setOrderStatus(),{
                     },{

                     })
                     showToast(it.result)
                    }
                }
                }.show()
        }

    }

    private fun readOrders() {
        homeViewModel.getPendingOrders(CommonRequestModel(sessionManager.getUser()?.business_id,OrderStatus.PENDING.status.toString())).observe(viewLifecycleOwner){
            if (it.getErrorIfExists()==null)
                getViewDataBinding().error = it.getErrorIfExists()?.message

            it.getErrorIfExists()?.let {
                showLoading(false)
                getViewDataBinding().error = it.message
            }

            it.getValueOrNull()?.let {
                showLoading(false)
                getViewDataBinding().swipeToRefresh.isRefreshing = false
                adapter.submitList(it.result)
                adapter.notifyDataSetChanged()

                if (it.status==0){
                    showLoading(false)
                    getViewDataBinding().isEmpty = true

                }

            }
        }
    }


    override fun getLayoutId() = R.layout.fragment_pending_orders
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
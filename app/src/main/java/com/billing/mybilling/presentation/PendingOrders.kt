package com.billing.mybilling.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.data.model.response.PendingOrders
import com.billing.mybilling.data.remote.PendingOrdersViewModel
import com.billing.mybilling.databinding.FragmentPendingOrdersBinding
import com.billing.mybilling.presentation.adapter.PendingOrdersAdapter
import com.billing.mybilling.presentation.adapter.PendingOrdersPagingAdapter
import com.billing.mybilling.session.SessionManager
import com.billing.mybilling.utils.OrderStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class PendingOrders: BaseFragment<FragmentPendingOrdersBinding,HomeViewModel>() {
    val homeViewModel: HomeViewModel by activityViewModels()

    @Inject
    lateinit var adapter: PendingOrdersAdapter
    @Inject
    lateinit var sessionManager: SessionManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewDataBinding().rvOrders.adapter = adapter

        showLoading(true)
        homeViewModel.getPendingOrders(CommonRequestModel(sessionManager.getUser()?.business_id,OrderStatus.PENDING.status.toString())).observe(viewLifecycleOwner){
            if (it.getErrorIfExists()==null)
                getViewDataBinding().error = it.getErrorIfExists()?.message

            it.getErrorIfExists()?.let {
                showLoading(false)
                getViewDataBinding().error = it.message
            }

            it.getValueOrNull()?.let {
                showLoading(false)
                adapter.submitList(it.result)
                if (it.status==0){
                    showLoading(false)
                    getViewDataBinding().isEmpty = true
                }

            }
        }

        adapter.open = {id,item->
            homeViewModel.pendingOrders = item
            findNavController().navigate(PendingOrdersDirections.actionPendingOrdersToPendingOrdersDetailsFragment(id!!,"Table No : "+item?.table_no))
        }

        getViewDataBinding().fabAdd.setOnClickListener {
            findNavController().navigate(PendingOrdersDirections.actionPendingOrdersToAddOrderFragment2())
        }

    }


    override fun getLayoutId() = R.layout.fragment_pending_orders
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
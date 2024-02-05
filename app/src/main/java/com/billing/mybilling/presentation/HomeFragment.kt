package com.billing.mybilling.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.utils.SingleLiveDataEvent
import com.billing.mybilling.databinding.FragmentHomeBinding
import com.billing.mybilling.presentation.adapter.PendingOrdersAdapter
import com.billing.mybilling.session.SessionManager
import com.billing.mybilling.utils.OrderStatus
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment:BaseFragment<FragmentHomeBinding,HomeViewModel>() {
    private val homeViewModel:HomeViewModel by viewModels()
    @Inject
    lateinit var sessionManager: SessionManager
    @Inject
    lateinit var adapter: PendingOrdersAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        getViewDataBinding().manageProduct.setOnClickListener {
//            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToViewCategoryListFragment())
//        }
//        getViewDataBinding().pending.setOnClickListener {
//            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPendingOrders())
//        }
//        getViewDataBinding().completedOrders.setOnClickListener {
//            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDeliveredOrdersFragment())
//        }
//        getViewDataBinding().manageUsers.setOnClickListener {
//            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToStaffListFragment())
//        }
        getViewDataBinding().rvOrders.adapter = adapter
        getViewDataBinding().manageProducts.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToViewCategoryListFragment())
        }
        getViewDataBinding().pendingOrders.setOnClickListener {
//            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPendingOrders())
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDeliveredOrdersFragment())
        }
        getViewDataBinding().manageUsers.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToStaffListFragment())
        }
        getViewDataBinding().history.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDeliveredOrdersFragment())
        }
        getViewDataBinding().add.setOnClickListener {
           findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddOrderFragment2())
        }

        getOrders()
        adapter.open = {id,item->
            homeViewModel.pendingOrders = item
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPendingOrdersDetailsFragment(id!!,"Table No : "+item?.table_no))
        }
        getViewDataBinding().layoutError.btnRetry.setOnClickListener {
            getOrders()
        }
    }

    private fun getOrders() {
        showLoading(true)
        homeViewModel.getPendingOrders(
            CommonRequestModel(sessionManager.getUser()?.business_id,
                OrderStatus.PENDING.status.toString())
        ).observe(viewLifecycleOwner){
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
    }

    override fun getLayoutId() = R.layout.fragment_home
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
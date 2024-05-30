package com.billing.mybilling.presentation


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.data.model.response.PendingOrders
import com.billing.mybilling.databinding.FragmentHomeBinding
import com.billing.mybilling.presentation.adapter.PendingTableOrdersAdapter
import com.billing.mybilling.session.SessionManager
import com.billing.mybilling.utils.OrderStatus
import com.billing.mybilling.utils.OrderType
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment:BaseFragment<FragmentHomeBinding,HomeViewModel>() {
    private val homeViewModel:HomeViewModel by activityViewModels()
    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var tableAdapter: PendingTableOrdersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getViewDataBinding().rvTable.adapter = tableAdapter
        getViewDataBinding().manageProducts.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToViewCategoryListFragment())
        }
        getViewDataBinding().pendingOrders.setOnClickListener {
//            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPendingOrders())
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDeliveredOrdersFragment())

        }
        getViewDataBinding().manageUsers.setOnClickListener {
//            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToStaffListFragment())
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAttendanceDashBoardFragment())
        }

        getViewDataBinding().analytics.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAnalyticsFragment())
        }

        getViewDataBinding().imgQrCode.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToQrCodeFragment())
        }

        val number = "+916378999146"
        getViewDataBinding().contactUs.setOnClickListener {
//            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDeliveredOrdersFragment())

            val url = "https://api.whatsapp.com/send?phone=$number"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)

//            showRatingDialog(requireActivity(),layoutInflater,"Rate our app",{
//                showToast("rating is given ")
//            },{
//
//            }).show()
        }
        getViewDataBinding().add.setOnClickListener {
           findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddOrderFragment2())
//           createPdf()
        }

        getOrders()

        tableAdapter.open = {id,item->
            homeViewModel.pendingOrders = item
            val orderOn = if (item!!.order_on==OrderType.TABLE.type) "Table No : "+item.table_no else "Packing"
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPendingOrdersDetailsFragment(id!!,orderOn,Gson().toJson(item)))

        }
        getViewDataBinding().layoutError.btnRetry.setOnClickListener {
            getOrders()
        }

        getViewDataBinding().swipeToRefresh.setOnRefreshListener {
            getOrders()
        }


    }

    private fun getOrders() {
//        showLoading(true)
        getViewDataBinding().progressBar.visibility = View.VISIBLE
        homeViewModel.getPendingOrders(
            CommonRequestModel(sessionManager.getUser()?.business_id,
                OrderStatus.PENDING.status.toString())
        ).observe(viewLifecycleOwner){
            if (it.getErrorIfExists()==null)
                getViewDataBinding().error = it.getErrorIfExists()?.message

            it.getErrorIfExists()?.let {
//                showLoading(false)
                getViewDataBinding().progressBar.visibility = View.GONE
                getViewDataBinding().error = it.message
                getViewDataBinding().swipeToRefresh.isRefreshing = false
            }

            it.getValueOrNull()?.let {
//                showLoading(false)
                getViewDataBinding().progressBar.visibility = View.GONE
                getViewDataBinding().swipeToRefresh.isRefreshing = false
                tableAdapter.submitList(it.result)
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
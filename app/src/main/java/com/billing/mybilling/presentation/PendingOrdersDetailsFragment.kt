package com.billing.mybilling.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.data.model.response.PendingOrders
import com.billing.mybilling.data.model.response.Products
import com.billing.mybilling.databinding.FragmentPendingOrdersDetailsBinding
import com.billing.mybilling.presentation.adapter.ProductsAdapter
import com.billing.mybilling.utils.*
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PendingOrdersDetailsFragment :
    BaseFragment<FragmentPendingOrdersDetailsBinding, HomeViewModel>() {
    val homeViewModel: HomeViewModel by activityViewModels()
    val args: PendingOrdersDetailsFragmentArgs by navArgs()

    @Inject
    lateinit var adapter: ProductsAdapter

    var orderItemList: List<Products>? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getViewDataBinding().recyclerView.adapter = adapter
//        showLoading(true)
        getViewDataBinding().progressBar.visibility = View.VISIBLE
        homeViewModel.getPendingOrderProducts(CommonRequestModel(args.id))
            .observe(viewLifecycleOwner) {
                Log.d("myError", "onViewCreated: " + it.getErrorIfExists())
                it.getErrorIfExists()?.let {
//                    showLoading(false)
                    getViewDataBinding().progressBar.visibility = View.GONE
                }
                it.getValueOrNull()?.let {
//                    showLoading(false)
                    getViewDataBinding().progressBar.visibility = View.GONE
                    orderItemList = it.result
                    adapter.submitList(it.result)
                    homeViewModel.pendingOrders?.products = it.result
                    if (it.status == 1) {
                        homeViewModel.updateTotalAmount(it.getTotalAmount())
                        getViewDataBinding().checkout.visibility = View.VISIBLE
                        getViewDataBinding().cancelOrder.visibility = View.VISIBLE
                    }else{
                        getViewDataBinding().checkout.visibility = View.GONE
                        getViewDataBinding().cancelOrder.visibility = View.GONE
                    }
                }
            }
//            showToast(""+homeViewModel.pendingOrders?.tableId)
        getViewDataBinding().addItem.setOnClickListener {
//            findNavController().navigate(PendingOrdersDetailsFragmentDirections.actionPendingOrdersDetailsFragmentToSearchProductFragment(args.id))
            findNavController().navigate(PendingOrdersDetailsFragmentDirections.actionPendingOrdersDetailsFragmentToCategoryFragment())
        }


        adapter.open = { it, type ->

            it?.product_order_id = args.id
            homeViewModel.updateOrderProduct(type, it!!).observe(viewLifecycleOwner) {
                it.getValueOrNull()?.let {

                }
            }
        }



        adapter.getData = { it, quan ->
            homeViewModel.updateTotalAmount(homeViewModel.totalAmount.value!! + (it!!.price!!.toInt() * quan))
        }
        getViewDataBinding().fragment = this
        homeViewModel.isPaymentOnline.observe(viewLifecycleOwner) {
            homeViewModel.updatePendingOrder(
                SelectedAction.UPDATE.type,
                homeViewModel.pendingOrders
            ).observe(viewLifecycleOwner) {

            }
        }
    }

    fun addCheckout() {
        if (orderItemList == null) {
            showToast("Can't checkout without any product")
        } else {
//            homeViewModel.pendingOrders?.order_status = OrderStatus.DELIVERED.status
            showLoading(true)
            val dialog = showWarningDialog(
                requireActivity(),
                layoutInflater,
                getString(R.string.warning_complete_order),
                {
//                homeViewModel.updatePendingOrder(SelectedAction.UPDATE.type,homeViewModel.pendingOrders).observe(viewLifecycleOwner){
//                    it.getValueOrNull()?.let {
//                        showLoading(false)
//                        if(it.status==1){
//                            showToast("Order completed successfully")
//                            findNavController().popBackStack()
//                        }
//                    }
//                }
                    showLoading(false)
                    findNavController().navigate(PendingOrdersDetailsFragmentDirections.actionPendingOrdersDetailsFragmentToOrdersBillingFragment(args.title))
                },
                {
                    showLoading(false)
                })

            dialog.show()
        }

    }

    fun cancelOrder() {
        homeViewModel.pendingOrders?.order_status = OrderStatus.FAILED.status

        val dialog = showWarningDialog(
            requireActivity(),
            layoutInflater,
            getString(R.string.warning_complete_order),
            {
                showLoading(true)
                homeViewModel.pendingOrders?.order_status = OrderStatus.FAILED.status
                homeViewModel.updatePendingOrder(
                    SelectedAction.UPDATE_STATUS.type,
                    homeViewModel.pendingOrders
                ).observe(viewLifecycleOwner) {
                    it.getErrorIfExists()?.let {
                        showToast("${it.message}")
                        showLoading(false)
                    }
                    it.getValueOrNull()?.let {
                        showLoading(false)
                        if (it.status == 1) {

                            showToast("Order cancelled successfully")
                            findNavController().popBackStack()
                        }
                    }
                }
            },
            {

            })

        dialog.show()
    }

    fun submit(){
        findNavController().popBackStack()
    }
//
//    fun addDeliveryCharge(){
//        findNavController().navigate(PendingOrdersDetailsFragmentDirections.actionPendingOrdersDetailsFragmentToAddDiscountFormFragment(editType.DELIVERY_CHARGES.type))
//    }

    override fun onDestroy() {
        super.onDestroy()
        homeViewModel.totalAmount.value = 0
    }

    override fun getLayoutId() = R.layout.fragment_pending_orders_details
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
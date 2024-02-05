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
import com.billing.mybilling.databinding.FragmentPendingOrdersDetailsBinding
import com.billing.mybilling.presentation.adapter.ProductsAdapter
import com.billing.mybilling.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PendingOrdersDetailsFragment: BaseFragment<FragmentPendingOrdersDetailsBinding,HomeViewModel>() {
    val homeViewModel: HomeViewModel by activityViewModels()
    val args:PendingOrdersDetailsFragmentArgs by navArgs()

    @Inject
    lateinit var adapter: ProductsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        homeViewModel.pendingOrders?.let {
            getViewDataBinding().model2 = homeViewModel.pendingOrders
        }
        getViewDataBinding().recyclerView.adapter = adapter
        showLoading(true)
        homeViewModel.getPendingOrderProducts(CommonRequestModel(args.id)).observe(viewLifecycleOwner){
            Log.d("myError", "onViewCreated: "+it.getErrorIfExists())
            it.getErrorIfExists()?.let {
                showLoading(false)
            }
            it.getValueOrNull()?.let {
                showLoading(false)
            adapter.submitList(it.result)
                if(it.status==1){
                    homeViewModel.updateTotalAmount(it.getTotalAmount())
                }
            }
        }

        getViewDataBinding().addItem.setOnClickListener {
            findNavController().navigate(PendingOrdersDetailsFragmentDirections.actionPendingOrdersDetailsFragmentToSearchProductFragment())
        }


        adapter.open = {it, type ->

            it?.product_order_id = homeViewModel.pendingOrders?.order_id

            homeViewModel.updateOrderProduct(type,it!!).observe(viewLifecycleOwner){
                it.getValueOrNull()?.let {

                }
            }
        }

        homeViewModel.totalAmount.observe(viewLifecycleOwner){
            getViewDataBinding().totalPrice.text = (it).toString().setPrice()

            val customerDiscount = homeViewModel.pendingOrders?.customer_discount ?: 0
            val deliveryCharges = homeViewModel.pendingOrders?.delivery_charges ?: 0
            val finalAmount = it - customerDiscount + deliveryCharges
            getViewDataBinding().name.text = homeViewModel.pendingOrders?.customer_name
            getViewDataBinding().totalDiscount.text = "- ${customerDiscount.toString().setPrice()}"
            getViewDataBinding().deliveryCharge.text = homeViewModel.pendingOrders?.delivery_charges.toString().setPrice()
            getViewDataBinding().finalAmount.text = finalAmount.toString().setPrice()
        }

        adapter.getData ={it,quan->
        homeViewModel.updateTotalAmount(homeViewModel.totalAmount.value!!+(it!!.price!!.toInt()*quan))
        }
        getViewDataBinding().fragment = this
        homeViewModel.isPaymentOnline.observe(viewLifecycleOwner){
            homeViewModel.updatePendingOrder(homeViewModel.pendingOrders).observe(viewLifecycleOwner){

            }
        }
    }
    fun addCheckout(){
        homeViewModel.pendingOrders?.order_status = OrderStatus.DELIVERED.status

        val dialog = showWarningDialog(requireActivity(),layoutInflater,getString(R.string.warning_complete_order),{

        },{
            homeViewModel.updatePendingOrder(homeViewModel.pendingOrders).observe(viewLifecycleOwner){
                it.getValueOrNull()?.let {
                    if(it.status==1){
                        showToast("Order completed successfully")
                        findNavController().popBackStack()
                    }
                }
            }
        })

        dialog.show()
    }

    fun cancelOrder(){
        homeViewModel.pendingOrders?.order_status = OrderStatus.FAILED.status

        val dialog = showWarningDialog(requireActivity(),layoutInflater,getString(R.string.warning_complete_order),{

        },{
            homeViewModel.updatePendingOrder(homeViewModel.pendingOrders).observe(viewLifecycleOwner){
                it.getValueOrNull()?.let {
                    if(it.status==1){
                        showToast("Order cancelled successfully")
                        findNavController().popBackStack()
                    }
                }
            }
        })

        dialog.show()
    }

    fun addDiscount(){
        findNavController().navigate(PendingOrdersDetailsFragmentDirections.actionPendingOrdersDetailsFragmentToAddDiscountFormFragment(editType.DISCOUNT.type))
    }

    fun addDeliveryCharge(){
        findNavController().navigate(PendingOrdersDetailsFragmentDirections.actionPendingOrdersDetailsFragmentToAddDiscountFormFragment(editType.DELIVERY_CHARGES.type))
    }
    override fun getLayoutId() = R.layout.fragment_pending_orders_details
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
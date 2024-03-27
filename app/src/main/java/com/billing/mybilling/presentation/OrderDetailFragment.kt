package com.billing.mybilling.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.AddOrderModel
import com.billing.mybilling.data.model.response.PendingOrders
import com.billing.mybilling.databinding.FragmentAddOrderBinding
import com.billing.mybilling.databinding.ItemOrderDetailBinding
import com.billing.mybilling.notification.sendNotificationToOrder
import com.billing.mybilling.presentation.adapter.OrderItemAdapter
import com.billing.mybilling.session.SessionManager
import com.billing.mybilling.utils.OrderType
import com.billing.mybilling.utils.setOrderOn
import com.billing.mybilling.utils.setOrderStatus
import com.billing.mybilling.utils.setPrice
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OrderDetailFragment: BaseFragment<ItemOrderDetailBinding,HomeViewModel>() {
    val homeViewModel: HomeViewModel by viewModels()
    @Inject
    lateinit var sessionManager: SessionManager
    val args:OrderDetailFragmentArgs by navArgs()

    @Inject
    lateinit var adapter:OrderItemAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.item!="na"){
            getViewDataBinding().recyclerview.adapter = adapter
            val model = Gson().fromJson(args.item,PendingOrders::class.java)
            getViewDataBinding().txtOrderNo.text = "Customer Name : ${model.customer_name}"
            getViewDataBinding().txtDateTime.text = model.formatDateToTimeAgo()
            getViewDataBinding().txtOrderStatus.text = "Order Status : ${model.order_status.setOrderStatus()}"
            getViewDataBinding().orderType.text = "Order type : ${model.order_on?.setOrderOn()}"
            getViewDataBinding().txtItemCount.text = "Items X ${model.products?.size} = "
            getViewDataBinding().txtPrice.text = model.getTotalAmount().toString().setPrice()
            adapter.submitList(model.products)
        }


    }



    override fun getLayoutId() = R.layout.item_order_detail
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
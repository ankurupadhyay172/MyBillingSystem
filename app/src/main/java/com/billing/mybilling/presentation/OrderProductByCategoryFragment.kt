package com.billing.mybilling.presentation

import android.R.attr
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.data.model.request.SearchRequestModel
import com.billing.mybilling.databinding.FragmentOrderProductByCategoryBinding
import com.billing.mybilling.databinding.FragmentSerachProductBinding
import com.billing.mybilling.presentation.adapter.ProductsAdapter
import com.billing.mybilling.session.SessionManager
import com.billing.mybilling.utils.LoadingState
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class OrderProductByCategoryFragment: BaseFragment<FragmentOrderProductByCategoryBinding,HomeViewModel>() {
    val homeViewModel: HomeViewModel by activityViewModels()
    @Inject
    lateinit var adapter:ProductsAdapter

    @Inject
    lateinit var sessionManager: SessionManager

    val args:OrderProductByCategoryFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)
        getViewDataBinding().recyclerView.adapter = adapter
            homeViewModel.getProductByCategory(SearchRequestModel(args.categoryId,homeViewModel.pendingOrders?.order_id,sessionManager.getUser()?.business_id)).observe(viewLifecycleOwner){
                it.getErrorIfExists()?.let {
                    showLoading(false)
                    showToast("${it.message}")
                }
                it.getValueOrNull()?.let {
                    showLoading(false)
                    homeViewModel.loadState.postValue(LoadingState.Loaded)
                    adapter.submitList(it.result)
                    getViewDataBinding().isEmpty = it.status==0
                }
            }

        getViewDataBinding().submit.setOnClickListener {
            findNavController().navigate(OrderProductByCategoryFragmentDirections.actionOrderProductByCategoryFragmentToPendingOrdersDetailsFragment(homeViewModel.pendingOrders?.order_id))
        }

        adapter.open = {it,type->
            it?.product_order_id = homeViewModel.pendingOrders?.order_id
            it?.business_id = sessionManager.getUser()?.business_id
            homeViewModel.updateOrderProduct(type,it!!).observe(viewLifecycleOwner){
                it.getValueOrNull()?.let {
                    showToast(""+it.result)
                }
            }
        }

    }




    override fun getLayoutId() = R.layout.fragment_order_product_by_category
    override fun getBindingVariable() = BR.homeViewModel
    override fun getViewModel() = homeViewModel
}
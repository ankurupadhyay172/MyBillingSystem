package com.billing.mybilling.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.AddOrderModel
import com.billing.mybilling.databinding.FragmentAddOrderBinding
import com.billing.mybilling.session.SessionManager
import com.billing.mybilling.utils.OrderType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddOrderFragment: BaseFragment<FragmentAddOrderBinding,HomeViewModel>() {
    val homeViewModel: HomeViewModel by viewModels()
    @Inject
    lateinit var sessionManager: SessionManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewDataBinding().isTable = true
        getViewDataBinding().fragment = this

        val table  = if (getViewDataBinding().isTable!!) OrderType.TABLE.type else OrderType.PACKING.type

        getViewDataBinding().checkout.setOnClickListener {
            if (getViewDataBinding().edtTableNo.text.toString().isEmpty()){
                showToast("Please enter the table no")
            }else{
                val name = getViewDataBinding().edtCustomerName.text.toString()
                val tableNo = getViewDataBinding().edtTableNo.text.toString()
                val mobile = getViewDataBinding().edtCustomerMobile.text.toString()
                showLoading(true)
                homeViewModel.createOrder(AddOrderModel(name,tableNo,mobile,table,sessionManager.getUser()?.user_id,sessionManager.getUser()?.business_id)).observe(viewLifecycleOwner){
                    it.getValueOrNull()?.let {
                        showToast(""+it.result)
                        showLoading(false)
                        if (it.status==1){
                            findNavController().popBackStack()
                        }

                    }
                }
            }
        }



    }
    fun onTableClick(){
        getViewDataBinding().isTable = true
    }
    fun onPackingClick(){
        getViewDataBinding().isTable = false
    }

    override fun getLayoutId() = R.layout.fragment_add_order
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
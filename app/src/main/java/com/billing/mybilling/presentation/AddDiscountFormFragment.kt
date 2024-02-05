package com.billing.mybilling.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.databinding.FragmentAddDiscountFormBinding
import com.billing.mybilling.utils.editType

class AddDiscountFormFragment: BaseFragment<FragmentAddDiscountFormBinding,HomeViewModel>() {
    val homeViewModel:HomeViewModel by activityViewModels()
    val args:AddDiscountFormFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateTitle()
        when (args.id) {
            editType.DISCOUNT.type -> getViewDataBinding().discount.setText(homeViewModel.pendingOrders?.customer_discount.toString())
            editType.DELIVERY_CHARGES.type -> getViewDataBinding().discount.setText(homeViewModel.pendingOrders?.delivery_charges.toString())
        }


        getViewDataBinding().submit.setOnClickListener {
            if (getViewDataBinding().discount.text.toString().isEmpty()){
                showToast("Please enter some amount")
            }else{
                val discount = getViewDataBinding().discount.text.toString().toInt()
                when (args.id) {
                    editType.DISCOUNT.type -> homeViewModel.pendingOrders?.customer_discount = discount
                    editType.DELIVERY_CHARGES.type -> homeViewModel.pendingOrders?.delivery_charges = discount
                }

                homeViewModel.updatePendingOrder(homeViewModel.pendingOrders).observe(viewLifecycleOwner){
                    it?.getValueOrNull()?.let {
                        findNavController().popBackStack()
                    }
                }

            }
        }
    }

    private fun updateTitle() {
        getViewDataBinding().title = args.id
    }


    override fun getLayoutId() = R.layout.fragment_add_discount_form
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
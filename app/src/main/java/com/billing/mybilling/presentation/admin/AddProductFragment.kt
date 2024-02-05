package com.billing.mybilling.presentation.admin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.AddProductRequestModel
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.data.model.response.Products
import com.billing.mybilling.databinding.FragmentAddCategoryFormBinding
import com.billing.mybilling.databinding.FragmentAddDiscountFormBinding
import com.billing.mybilling.databinding.FragmentAddProductFormBinding
import com.billing.mybilling.presentation.HomeViewModel
import com.billing.mybilling.utils.SelectedAction
import com.billing.mybilling.utils.editType
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProductFragment: BaseFragment<FragmentAddProductFormBinding, HomeViewModel>() {
    val homeViewModel:HomeViewModel by viewModels()
    val args:AddProductFragmentArgs by navArgs()
    var model:Products? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewDataBinding().title = "Add Product"

        if (args.model!="na"){
            model = Gson().fromJson(args.model,Products::class.java)
            getViewDataBinding().edtTitle.setText(model?.product_name)
        }

        getViewDataBinding().submit.setOnClickListener {
            if (getViewDataBinding().edtTitle.text.toString().isEmpty()){
                showToast("Please enter product name")
            }else{
                showLoading(true)
                if (model!=null){
                    homeViewModel.addProduct(SelectedAction.UPDATE.type,AddProductRequestModel(model?.pid,getViewDataBinding().edtTitle.text.toString(),0)).observe(viewLifecycleOwner){
                        it.getValueOrNull()?.let {
                            if (it.status==1){
                                findNavController().popBackStack()
                            }
                            showToast("${it.result}")
                            showLoading(false)
                        }
                    }
                }else{
                    homeViewModel.addProduct(SelectedAction.ADD.type,AddProductRequestModel(args.id,getViewDataBinding().edtTitle.text.toString(),0)).observe(viewLifecycleOwner){
                        it.getValueOrNull()?.let {
                            showToast("${it.result}")
                            findNavController().popBackStack()
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }




    override fun getLayoutId() = R.layout.fragment_add_product_form
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
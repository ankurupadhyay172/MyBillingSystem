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
import com.billing.mybilling.data.model.request.AddVariantRequestModel
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.data.model.response.Variants
import com.billing.mybilling.databinding.FragmentAddCategoryFormBinding
import com.billing.mybilling.databinding.FragmentAddDiscountFormBinding
import com.billing.mybilling.databinding.FragmentAddProductFormBinding
import com.billing.mybilling.databinding.FragmentAddVariantFormBinding
import com.billing.mybilling.presentation.HomeViewModel
import com.billing.mybilling.utils.SelectedAction
import com.billing.mybilling.utils.editType
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProductVariantFragment: BaseFragment<FragmentAddVariantFormBinding, HomeViewModel>() {
    val homeViewModel:HomeViewModel by viewModels()
    val args:AddProductVariantFragmentArgs by navArgs()
    var model: Variants? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.model!="na"){
            model = Gson().fromJson(args.model,Variants::class.java)
            getViewDataBinding().edtTitle.setText(model?.variant_name)
            getViewDataBinding().edtPrice.setText(model?.price)
        }

        getViewDataBinding().submit.setOnClickListener {
            if (getViewDataBinding().edtTitle.text.toString().isEmpty()){
                showToast("Please enter product name")
            }else{
                showLoading(true)
                if (model==null){
                    homeViewModel.addVariant(SelectedAction.ADD.type,AddVariantRequestModel(args.id,getViewDataBinding().edtTitle.text.toString(),getViewDataBinding().edtPrice.text.toString())).observe(viewLifecycleOwner){
                        it.getValueOrNull()?.let {
                            showToast("${it.result}")
                            findNavController().popBackStack()
                            showLoading(false)
                        }
                    }
                }else{
                    homeViewModel.addVariant(SelectedAction.UPDATE.type,AddVariantRequestModel(model?.vid,getViewDataBinding().edtTitle.text.toString(),getViewDataBinding().edtPrice.text.toString())).observe(viewLifecycleOwner){
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




    override fun getLayoutId() = R.layout.fragment_add_variant_form
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
package com.billing.mybilling.presentation.admin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.data.model.response.Category
import com.billing.mybilling.databinding.FragmentAddCategoryFormBinding
import com.billing.mybilling.databinding.FragmentAddDiscountFormBinding
import com.billing.mybilling.presentation.HomeViewModel
import com.billing.mybilling.session.SessionManager
import com.billing.mybilling.utils.SelectedAction
import com.billing.mybilling.utils.editType
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddCategoryFragment: BaseFragment<FragmentAddCategoryFormBinding, HomeViewModel>() {
    val homeViewModel:HomeViewModel by activityViewModels()
    val args: AddCategoryFragmentArgs by navArgs()
    var model:Category? = null
    @Inject
    lateinit var sessionManager: SessionManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewDataBinding().title = "Add Product"
        if (args.id!="na"){
            model = Gson().fromJson(args.id,Category::class.java)
            getViewDataBinding().edtTitle.setText(model?.name)
        }

        getViewDataBinding().submit.setOnClickListener {
            if (getViewDataBinding().edtTitle.text.toString().isEmpty()){
                showToast("Please enter product name")
            }else{
                showLoading(true)
                if (model==null){
                    homeViewModel.addCategory(SelectedAction.ADD.type,CommonRequestModel(sessionManager.getUser()?.business_id,getViewDataBinding().edtTitle.text.toString())).observe(viewLifecycleOwner){
                        it.getValueOrNull()?.let {
                            showToast("${it.result}")
                            findNavController().popBackStack()
                            showLoading(false)
                        }
                    }
                }else{
                    homeViewModel.addCategory(SelectedAction.UPDATE.type,CommonRequestModel(model?.category_id,getViewDataBinding().edtTitle.text.toString())).observe(viewLifecycleOwner){
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




    override fun getLayoutId() = R.layout.fragment_add_category_form
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
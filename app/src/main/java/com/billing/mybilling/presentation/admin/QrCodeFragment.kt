package com.billing.mybilling.presentation.admin

import android.app.Activity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.AddCategoryRequestModel
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.data.model.response.Category
import com.billing.mybilling.databinding.FragmentAddCategoryFormBinding
import com.billing.mybilling.databinding.FragmentAddDiscountFormBinding
import com.billing.mybilling.databinding.FragmentQrCodeBinding
import com.billing.mybilling.presentation.HomeViewModel
import com.billing.mybilling.session.SessionManager
import com.billing.mybilling.utils.SelectedAction
import com.billing.mybilling.utils.editType
import com.billing.mybilling.utils.setImage
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class QrCodeFragment: BaseFragment<FragmentQrCodeBinding, HomeViewModel>() {
    val homeViewModel:HomeViewModel by activityViewModels()
    @Inject
    lateinit var sessionManager: SessionManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewDataBinding().image = sessionManager.getUser()?.business_payment_qr_code
    }




    override fun getLayoutId() = R.layout.fragment_qr_code
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
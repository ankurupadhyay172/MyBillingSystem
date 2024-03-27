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
import com.billing.mybilling.presentation.HomeViewModel
import com.billing.mybilling.session.SessionManager
import com.billing.mybilling.utils.SelectedAction
import com.billing.mybilling.utils.editType
import com.billing.mybilling.utils.setImage
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddCategoryFragment: BaseFragment<FragmentAddCategoryFormBinding, HomeViewModel>() {
    val homeViewModel:HomeViewModel by activityViewModels()
    val args: AddCategoryFragmentArgs by navArgs()
    var model:Category? = null
    var imageUrl:String? = null
    @Inject
    lateinit var sessionManager: SessionManager
    var isUpdate = false

    val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data
            getViewDataBinding().ivCategory.setImageURI(uri)
            val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
            homeViewModel.BitMapToString(bitmap)?.let {
            imageUrl = it
                isUpdate = true
            }
        }}
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewDataBinding().title = "Add Product"
        if (args.id!="na"){
            model = Gson().fromJson(args.id,Category::class.java)
            getViewDataBinding().edtTitle.setText(model?.name)
            getViewDataBinding().ivCategory.setImage(model?.image)

        }

        getViewDataBinding().submit.setOnClickListener {
            if (getViewDataBinding().edtTitle.text.toString().isEmpty()){
                showToast("Please enter product name")
            }else{

                showLoading(true)
                if (model==null){
                    homeViewModel.addCategory(SelectedAction.ADD.type,
                        AddCategoryRequestModel(sessionManager.getUser()?.business_id,getViewDataBinding().edtTitle.text.toString(),imageUrl)
                    ).observe(viewLifecycleOwner){
                        it.getValueOrNull()?.let {
                            showToast("${it.result}")
                            findNavController().popBackStack()
                            showLoading(false)
                        }
                    }
                }else{
                    homeViewModel.addCategory(SelectedAction.UPDATE.type,AddCategoryRequestModel(model?.category_id,getViewDataBinding().edtTitle.text.toString(),imageUrl,model?.image, isImageUpdate = isUpdate)).observe(viewLifecycleOwner){
                        it.getValueOrNull()?.let {
                            showToast("${it.result}")
                            findNavController().popBackStack()
                            showLoading(false)
                        }
                    }
                }

            }
        }

        getViewDataBinding().ivImage.setOnClickListener {
            requestImagePermission {
                openGallery(galleryLauncher)
            }
        }
    }




    override fun getLayoutId() = R.layout.fragment_add_category_form
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
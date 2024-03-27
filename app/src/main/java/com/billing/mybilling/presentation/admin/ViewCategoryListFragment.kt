package com.billing.mybilling.presentation.admin

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.AddCategoryRequestModel
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.database.DatabaseManager
import com.billing.mybilling.databinding.FragmentProductListBinding
import com.billing.mybilling.databinding.FragmentViewCategoryListBinding
import com.billing.mybilling.presentation.HomeViewModel
import com.billing.mybilling.presentation.adapter.ProductsAdapter
import com.billing.mybilling.presentation.adapter.ViewCategoryAdapter
import com.billing.mybilling.session.SessionManager
import com.billing.mybilling.utils.SelectedAction
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ViewCategoryListFragment: BaseFragment<FragmentViewCategoryListBinding, HomeViewModel>() {
    val homeViewModel: HomeViewModel by viewModels()
    @Inject
    lateinit var adapter: ViewCategoryAdapter
    @Inject
    lateinit var sessionManager: SessionManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewDataBinding().recyclerview.adapter = adapter





//        lifecycleScope.launch(Dispatchers.IO) {
//            homeViewModel.getCategoryList()
//        }
//
//        databaseManager.getCategoriesLive().observe(viewLifecycleOwner){
//            adapter.submitList(it)
//        }
       updateCategory()
        getViewDataBinding().fabAdd.setOnClickListener {
            findNavController().navigate(ViewCategoryListFragmentDirections.actionViewCategoryListFragmentToAddCategoryFragment())
        }
        adapter.open = {id,category->
            findNavController().navigate(ViewCategoryListFragmentDirections.actionViewCategoryListFragmentToProductListFragment(id))
        }
        adapter.option = { model ->
            AlertDialog.Builder(requireContext()).setTitle(R.string.options)
                .setItems(R.array.options) { dialog, which ->
                    when(which){
                        0-> {
                            findNavController().navigate(ViewCategoryListFragmentDirections.actionViewCategoryListFragmentToAddCategoryFragment(Gson().toJson(model)))
                        }
                        1-> {
                            showLoading(true)
                            homeViewModel.addCategory(SelectedAction.DELETE.type, AddCategoryRequestModel(model?.category_id)).observe(viewLifecycleOwner){
                                it.getValueOrNull()?.let {
                                    if (it.status==1){
                                        showLoading(false)
                                        showToast("Category deleted successfully")
                                        updateCategory()
                                    }else{
                                        showToast(it.result)
                                    }
                                    showLoading(false)
                                }
                            }
                        }
                    }
                }.show()
        }
    }

    private fun updateCategory() {
        showLoading(true)

        homeViewModel.getCategoriesList(CommonRequestModel(sessionManager.getUser()?.business_id)).observe(viewLifecycleOwner){
            it.getErrorIfExists()?.let {
                showLoading(false)
                getViewDataBinding().error = it.message
            }
            it.getValueOrNull()?.let {
                showLoading(false)
                adapter.submitList(it.result)
                if (it.status==0){
                    getViewDataBinding().isEmpty = true
                }
            }
        }

    }

    override fun getLayoutId() = R.layout.fragment_view_category_list
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
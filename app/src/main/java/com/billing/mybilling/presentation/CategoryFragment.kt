package com.billing.mybilling.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.database.DatabaseManager
import com.billing.mybilling.databinding.FragmentCategoryBinding
import com.billing.mybilling.presentation.adapter.CategoryAdapter
import com.billing.mybilling.utils.SelectedAction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class CategoryFragment: BaseFragment<FragmentCategoryBinding,HomeViewModel>() {
    private val homeViewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var adapter:CategoryAdapter

    @Inject
    lateinit var databaseManager: DatabaseManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getViewDataBinding().rvCategory.adapter = adapter

        updateCategory()

        adapter.option = {model->
            AlertDialog.Builder(requireContext()).setTitle(R.string.options)
                .setItems(R.array.options) { dialog, which ->
                 when(which){
                     0-> {

                     }
                     1-> {
                         showLoading(true)
                         homeViewModel.addCategory(SelectedAction.DELETE.type, CommonRequestModel(model?.category_id)).observe(viewLifecycleOwner){
                             it.getValueOrNull()?.let {
                                 if (it.status==1){
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

//        lifecycleScope.launch {
//            homeViewModel.getCategoryList()
//        }

//        databaseManager.getCategoriesLive().observe(viewLifecycleOwner){
//            adapter.submitList(it)
//        }

        adapter.open = {
            //findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToSearchProductFragment())
            findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToProductListFragment(it))
        }
    }

    private fun updateCategory() {
//        homeViewModel.getCategoriesList().observe(viewLifecycleOwner){
//            it.getValueOrNull()?.let {
//                adapter.submitList(it.result)
//            }
//        }
    }


    override fun getLayoutId() = R.layout.fragment_category
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
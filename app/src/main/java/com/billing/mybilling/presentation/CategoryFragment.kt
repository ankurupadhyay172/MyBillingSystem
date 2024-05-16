package com.billing.mybilling.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.database.DatabaseManager
import com.billing.mybilling.databinding.FragmentCategoryBinding
import com.billing.mybilling.presentation.adapter.CategoryAdapter
import com.billing.mybilling.session.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class CategoryFragment: BaseFragment<FragmentCategoryBinding,HomeViewModel>() {
    private val homeViewModel: HomeViewModel by activityViewModels()

    @Inject
    lateinit var adapter:CategoryAdapter

    @Inject
    lateinit var databaseManager: DatabaseManager
    @Inject
    lateinit var sessionManager:SessionManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getViewDataBinding().rvCategory.adapter = adapter
//        showToast(""+homeViewModel.pendingOrders?.order_id)
        //updateCategory()
        getCategoryFromDatabase()
        getViewDataBinding().search.setOnClickListener {
            findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToSearchProductFragment(homeViewModel.pendingOrders?.order_id))
        }
        getViewDataBinding().edtSearch.setOnClickListener {
            findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToSearchProductFragment(homeViewModel.pendingOrders?.order_id))
        }
        showToast(""+homeViewModel.pendingOrders?.tableId)
//        lifecycleScope.launch {
//            homeViewModel.getCategoryList()
//        }

        databaseManager.getCategoriesLive().observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        adapter.open = {
            //findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToSearchProductFragment())
            //findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToProductListFragment(it))
            findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToOrderProductByCategoryFragment(it))
        }
    }

    private fun getCategoryFromDatabase() {
        lifecycleScope.launch(Dispatchers.IO){
            homeViewModel.getCategoriesListFromDatabase(CommonRequestModel(sessionManager.getUser()?.business_id))
        }

        databaseManager.getCategoriesLive().observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }

    private fun updateCategory() {
        showLoading(true)
        homeViewModel.getCategoriesList(CommonRequestModel(sessionManager.getUser()?.business_id)).observe(viewLifecycleOwner){
            it.getErrorIfExists()?.let {
                showLoading(false)
                showToast("${it.message}")
            }
            it.getValueOrNull()?.let {
                showLoading(false)
                adapter.submitList(it.result)
            }
        }
    }


    override fun getLayoutId() = R.layout.fragment_category
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
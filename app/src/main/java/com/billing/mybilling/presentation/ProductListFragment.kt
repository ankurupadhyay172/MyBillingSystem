package com.billing.mybilling.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.AddProductRequestModel
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.database.DatabaseManager
import com.billing.mybilling.databinding.FragmentProductListBinding
import com.billing.mybilling.presentation.adapter.ViewProductListAdapter
import com.billing.mybilling.utils.SelectedAction
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProductListFragment: BaseFragment<FragmentProductListBinding,HomeViewModel>() {
    val homeViewModel: HomeViewModel by viewModels()
    @Inject
    lateinit var adapter: ViewProductListAdapter
    @Inject
    lateinit var databaseManager: DatabaseManager
    val args:ProductListFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewDataBinding().apply {
            rvProduct.adapter = adapter
        }
            getViewDataBinding().layoutError.btnRetry.setOnClickListener {
                getProductsFromDatabase()
            }

            //getProducts()
            getProductsFromDatabase()

//        lifecycleScope.launch {
//            homeViewModel.getProductsList(CommonRequestModel(args.id))
//        }
//
//        showLoading(true)
//        databaseManager.getAllProductsLive(args.id).observe(viewLifecycleOwner){
//            getViewDataBinding().isEmpty = it.isEmpty()
//            showLoading(false)
//            adapter.submitList(it)
//        }

        adapter.open = {id->
            findNavController().navigate(ProductListFragmentDirections.actionProductListFragmentToViewVariantsListFragment(id))
        }

        getViewDataBinding().fabAdd.setOnClickListener {
            findNavController().navigate(ProductListFragmentDirections.actionProductListFragmentToAddProductFragment(args.id))
        }

        adapter.option = {model->
            AlertDialog.Builder(requireContext()).setTitle(R.string.options)
                .setItems(R.array.options) { dialog, which ->
                    when (which) {
                    0->{
                        findNavController().navigate(ProductListFragmentDirections.actionProductListFragmentToAddProductFragment(args.id,Gson().toJson(model)))
                    }

                    1->{
                        showLoading(true)
                        homeViewModel.addProduct(SelectedAction.DELETE.type,
                            AddProductRequestModel(args.id)
                        ).observe(viewLifecycleOwner){
                            it.getValueOrNull()?.let {
                                if (it.status==1){
                                 showToast("Item Deleted Successfully")
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

    private fun getProductsFromDatabase() {
        lifecycleScope.launch(Dispatchers.IO){
            homeViewModel.getProductsFromDatabase(CommonRequestModel(args.id))
        }
        databaseManager.getAllProductsLive(args.id).observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }

    private fun getProducts() {
        showLoading(true)
        homeViewModel.getProductsList(CommonRequestModel(args.id)).observe(viewLifecycleOwner){
            if (it.getErrorIfExists()==null)
                getViewDataBinding().error = it.getErrorIfExists()?.message

            it.getErrorIfExists()?.let {
                showLoading(false)
                getViewDataBinding().error = it.message
            }
            it.getValueOrNull()?.let {
                adapter.submitList(it.result)
                if (it.status==0)
                    getViewDataBinding().isEmpty = true
                showLoading(false)
            }
        }
    }

    override fun getLayoutId() = R.layout.fragment_product_list

    override fun getBindingVariable() = BR.model

    override fun getViewModel() = homeViewModel
}
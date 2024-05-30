package com.billing.mybilling.presentation.admin

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
import com.billing.mybilling.data.model.request.AddVariantRequestModel
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.database.DatabaseManager
import com.billing.mybilling.databinding.FragmentProductListBinding
import com.billing.mybilling.databinding.FragmentViewCategoryListBinding
import com.billing.mybilling.presentation.HomeViewModel
import com.billing.mybilling.presentation.adapter.ProductsAdapter
import com.billing.mybilling.presentation.adapter.ViewCategoryAdapter
import com.billing.mybilling.presentation.adapter.ViewVariantsAdapter
import com.billing.mybilling.utils.SelectedAction
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ViewVariantsListFragment : BaseFragment<FragmentProductListBinding, HomeViewModel>() {
    val homeViewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var adapter: ViewVariantsAdapter

    @Inject
    lateinit var databaseManager: DatabaseManager
    val args: ViewVariantsListFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewDataBinding().rvProduct.adapter = adapter
        showLoading(true)


       getVariantList()

        getViewDataBinding().fabAdd.setOnClickListener {
            findNavController().navigate(
                ViewVariantsListFragmentDirections.actionViewVariantsListFragmentToAddProductVariantFragment(
                    args.id
                )
            )
        }
        adapter.option = { model ->
            AlertDialog.Builder(requireContext()).setTitle(R.string.options)
                .setItems(R.array.options) { dialog, which ->
                    when (which) {
                        0 -> {
                            findNavController().navigate(
                                ViewVariantsListFragmentDirections.actionViewVariantsListFragmentToAddProductVariantFragment(
                                    args.id,
                                    Gson().toJson(model)
                                )
                            )
                        }

                        1 -> {
                            homeViewModel.addVariant(
                                SelectedAction.DELETE.type,
                                AddVariantRequestModel(model?.vid)
                            ).observe(viewLifecycleOwner) {
                                it.getValueOrNull()?.let {
                                    showToast("${it.result}")
                                    getVariantList()

                                }
                            }
                        }
                    }

                }.show()

        }
    }

    private fun getVariantList() {
        homeViewModel.getVariants(CommonRequestModel(args.id)).observe(viewLifecycleOwner) {
            it.getErrorIfExists()?.let {
                showLoading(false)
                getViewDataBinding().error = it.message
            }
            it.getValueOrNull()?.let {
                showLoading(false)
                adapter.submitList(it.result)
                if (it.status == 0)
                    getViewDataBinding().isEmpty = true
            }
        }
    }

    override fun getLayoutId() = R.layout.fragment_product_list
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
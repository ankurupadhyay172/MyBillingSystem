package com.billing.mybilling.presentation.login

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.utils.SingleLiveDataEvent
import com.billing.mybilling.databinding.FragmentHomeBinding
import com.billing.mybilling.databinding.FragmentStaffListBinding
import com.billing.mybilling.presentation.HomeViewModel
import com.billing.mybilling.presentation.adapter.StaffAdapter
import com.billing.mybilling.session.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StaffListFragment:BaseFragment<FragmentStaffListBinding,HomeViewModel>() {
    private val homeViewModel: HomeViewModel by viewModels()
    @Inject
    lateinit var sessionManager: SessionManager
    @Inject
    lateinit var adapter: StaffAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewDataBinding().recyclerview.adapter = adapter
        showLoading(true)
        sessionManager.getUser()?.let {
            homeViewModel.getStaffList(CommonRequestModel(it.business_id)).observe(viewLifecycleOwner){
                it.getValueOrNull()?.let {
                    showLoading(false)
                    adapter.submitList(it.result)
                }
            }
        }
        homeViewModel.selectedUser = null
        adapter.open = {
        homeViewModel.selectedUser = it
        }
        adapter.options ={model->
            AlertDialog.Builder(requireContext()).setTitle(R.string.options)
                .setItems(R.array.option_del) { dialog, which ->
                

                }.show()
        }
        getViewDataBinding().fabAdd.setOnClickListener {
            findNavController().navigate(StaffListFragmentDirections.actionStaffListFragmentToAddStaffFormFragment())
        }

    }

    override fun getLayoutId() = R.layout.fragment_staff_list
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
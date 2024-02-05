package com.billing.mybilling.presentation.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.response.Users
import com.billing.mybilling.presentation.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.billing.mybilling.databinding.FragmentAddStaffFormBinding
import com.billing.mybilling.session.SessionManager
import com.billing.mybilling.utils.SelectedAction
import com.billing.mybilling.utils.UserType
import javax.inject.Inject

@AndroidEntryPoint
class AddStaffFormFragment: BaseFragment<FragmentAddStaffFormBinding, HomeViewModel>() {
    val homeViewModel:HomeViewModel by activityViewModels()
    @Inject
    lateinit var sessionManager: SessionManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getViewDataBinding().submit.setOnClickListener {
            if(getViewDataBinding().name.text.isEmpty()||getViewDataBinding().userId.text.isEmpty()||
                    getViewDataBinding().password.text.isEmpty()){
                if (getViewDataBinding().name.text.isEmpty())
                    getViewDataBinding().name.error = "Please enter name"
                if (getViewDataBinding().userId.text.isEmpty())
                    getViewDataBinding().userId.error = "Please enter userid"
                if (getViewDataBinding().password.text.isEmpty())
                    getViewDataBinding().password.error = "Please enter password"
            }else{
                homeViewModel.selectedUser?.apply {
                    name = getViewDataBinding().name.text.toString()
                    user_id = getViewDataBinding().userId.text.toString()
                    pass = getViewDataBinding().password.text.toString()
                    business_id = sessionManager.getUser()?.business_id ?: ""
                    user_type_id = UserType.STAFF.id
                }
                val isUpdate = if (homeViewModel.selectedUser == null) SelectedAction.ADD.type else SelectedAction.UPDATE.type

                val users = Users(
                    name = getViewDataBinding().name.text.toString(),
                    user_id = getViewDataBinding().userId.text.toString(),
                    pass = getViewDataBinding().password.text.toString(),
                    user_type = UserType.STAFF.id.toString(),
                    business_id = sessionManager.getUser()?.business_id ?: ""
                )

                homeViewModel.updateUsers(isUpdate, users).observe(viewLifecycleOwner) {
                    it.getValueOrNull()?.let { response ->
                        showToast(if (response.status == 1) "User updated successfully" else "Something went wrong")
                        findNavController().popBackStack()
                        showLoading(false)
                    }
                }
            }
        }

        //homeViewModel.updateUsers(SelectedAction.ADD.type, )


    }

    override fun getLayoutId() = R.layout.fragment_add_staff_form
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
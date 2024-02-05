package com.billing.mybilling.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.response.Products
import com.billing.mybilling.data.model.request.CartRequestModel
import com.billing.mybilling.databinding.LayoutCartBinding
import com.billing.mybilling.presentation.adapter.ProductsAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class CartFragment: BaseFragment<LayoutCartBinding,HomeViewModel>() {
    private val homeViewModel:HomeViewModel by viewModels()
    @Inject
    lateinit var adapter: ProductsAdapter
    lateinit var list:List<Products>

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser){

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewDataBinding().isTable = true
        getViewDataBinding().fragment = this
        getViewDataBinding().recyclerview.adapter = adapter
            homeViewModel.databaseManager.getAllTheCartLive().observe(viewLifecycleOwner){
                list = it.map { it.product }
                adapter.submitList( list)
            }
        val table  = if (getViewDataBinding().isTable!!) "table" else "packing"


        getViewDataBinding().submit.setOnClickListener {
            if (getViewDataBinding().edtTableNo.text.toString().isEmpty()){
                showToast("Table No is mandatory")
            }else{
                adapter.open = {it,type->
                val userId = "101"
                val customerId = "101"
                val timeStamp = System.currentTimeMillis()
                val orderId = userId+customerId+timeStamp
                homeViewModel.addToCart(CartRequestModel(orderId,getViewDataBinding().edtCustomerName.text.toString(),
                getViewDataBinding().edtTableNo.text.toString(),getViewDataBinding().edtInstruction.text.toString(),
                getViewDataBinding().edtCustomerMobile.text.toString(),table,"ankur",list)).observe(viewLifecycleOwner){
                    it.getValueOrNull()?.let {
                        if(it.status==1){
                            showToast("successfully added in cart")
                        }
                    }
                }
                }
            }
        }
        }

    fun onTableClick(){
        getViewDataBinding().isTable = true
    }
    fun onPackingClick(){
        getViewDataBinding().isTable = false
    }


    override fun getLayoutId() = R.layout.layout_cart
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
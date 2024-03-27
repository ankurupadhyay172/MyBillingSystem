package com.billing.mybilling.presentation

import android.R.attr
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.data.model.request.SearchRequestModel
import com.billing.mybilling.databinding.FragmentSerachProductBinding
import com.billing.mybilling.presentation.adapter.ProductsAdapter
import com.billing.mybilling.session.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class SearchProductFragment: BaseFragment<FragmentSerachProductBinding,HomeViewModel>() {
    val homeViewModel: HomeViewModel by activityViewModels()
    @Inject
    lateinit var adapter:ProductsAdapter
    lateinit var edtSearch:EditText
    @Inject
    lateinit var sessionManager: SessionManager
    val args:SearchProductFragmentArgs by navArgs()
    val speechLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if (result.resultCode == RESULT_OK) {
            val res: ArrayList<String> =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
            edtSearch.setText(res[0])
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getViewDataBinding().recyclerView.adapter = adapter
        val mic = view.findViewById<ImageView>(R.id.mic)
        mic.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
            )
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")

            try {
                speechLauncher.launch(intent)
            } catch (e: Exception) {
                showToast(""+e.message)
            }
        }

        edtSearch = view.findViewById(R.id.input)
        edtSearch.doOnTextChanged { text, start, before, count ->
            homeViewModel.getSearchProduct(SearchRequestModel(text.toString(),args.orderId,sessionManager.getUser()?.business_id)).observe(viewLifecycleOwner){
                it.getValueOrNull()?.let {
                    adapter.submitList(it.result)
                }
            }
        }

        adapter.open = {it,type->
            it?.product_order_id = args.orderId
            it?.business_id = sessionManager.getUser()?.business_id
            homeViewModel.updateOrderProduct(type,it!!).observe(viewLifecycleOwner){
                it.getValueOrNull()?.let {
                    showToast(""+it.result)
                }
            }
        }

    }




    override fun getLayoutId() = R.layout.fragment_serach_product
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}
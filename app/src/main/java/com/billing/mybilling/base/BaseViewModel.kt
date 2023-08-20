package com.billing.mybilling.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.billing.mybilling.utils.SingleLiveDataEvent

open class BaseViewModel: ViewModel() {
    var errorLiveData: MutableLiveData<SingleLiveDataEvent<String>> = MutableLiveData()

}
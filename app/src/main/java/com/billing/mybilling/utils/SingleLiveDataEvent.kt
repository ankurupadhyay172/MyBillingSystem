package com.billing.mybilling.utils

import androidx.lifecycle.Observer

open class SingleLiveDataEvent<out T>(private val content: T) {

    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T?{
        return if (hasBeenHandled){
            null
        }else {
            hasBeenHandled = true
            content
        }
    }

    fun value() = getContentIfNotHandled()

    fun peekContent():T = content
}

class SingleLiveObserver<T>(private val onEventUnHandledContent: (T)->Unit):
        Observer<SingleLiveDataEvent<T>>{


    override fun onChanged(event: SingleLiveDataEvent<T>) {
        event.getContentIfNotHandled()?.let {value->
            onEventUnHandledContent(value)
        }
    }

}
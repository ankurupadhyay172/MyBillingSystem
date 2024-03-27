package com.billing.mybilling.test

import androidx.lifecycle.liveData
import com.billing.mybilling.data.model.response.CommonResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed class MyLoadingState<T>{

    object Loading:MyLoadingState<Nothing>()
    class Loaded<T> (val result:T):MyLoadingState<T>()
    class Error<T> (val error:Throwable):MyLoadingState<T>()

    val isLoading get() = this is Loading
    fun getValueOrNull():T? = if (this is Loaded) result else null
    fun getErrorOrNull():Throwable? = if (this is Error) error else null
}

    fun <T> Flow<T>.myLoading():Flow<MyLoadingState<T>>{
        return map<T,MyLoadingState<T>> { MyLoadingState.Loaded(it) }.onStart { emit(MyLoadingState.Loading as MyLoadingState<T>) }
            .catch { emit(MyLoadingState.Error(it)) }
    }
class MyTestRepository{

    fun getFinalUser()= liveData(Dispatchers.IO){
        getUserData().myLoading().collect{
            emit(it)
        }
    }
    fun getUserData() = flow {
        val response = CommonResponseModel(1,"")
        emit(response)
    }


}



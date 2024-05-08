package com.billing.mybilling.test

import androidx.lifecycle.liveData
import com.billing.mybilling.data.model.response.Users
import com.billing.mybilling.utils.LoadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed class MyLoadingState<T>{
    object Loading:MyLoadingState<Nothing>()
    class Loaded<T>(val value:T):MyLoadingState<T>()
    class Error<T>(val error:Throwable):MyLoadingState<T>()

    val isLoading get() = this is Loading
    fun getValueOrNull() = if(this is Loaded) value else null
    fun getErrorIfExists() = if(this is Error) error else null
}

fun <T> Flow<T>.toLoadingState():Flow<MyLoadingState<T>>{
    return map<T,MyLoadingState<T>> { MyLoadingState.Loaded(it) }.onStart {
        emit(MyLoadingState.Loading as MyLoadingState<T>)
    }.catch {e->
        emit(MyLoadingState.Error(e))
    }
}

fun getUserData():Flow<Users> = flow{
    val response = Users("1","","","","","","")
    emit(response)
}

fun getUserDataRepo()= liveData(Dispatchers.IO){
     getUserData().toLoadingState().collect{
         emit(it)
     }
}
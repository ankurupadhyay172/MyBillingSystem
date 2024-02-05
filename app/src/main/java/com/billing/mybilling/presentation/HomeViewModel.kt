package com.billing.mybilling.presentation

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.billing.mybilling.base.BaseViewModel
import com.billing.mybilling.data.model.request.*
import com.billing.mybilling.data.model.response.PendingOrders
import com.billing.mybilling.data.model.response.Products
import com.billing.mybilling.data.model.response.Users
import com.billing.mybilling.data.repository.HomeRepository
import com.billing.mybilling.data.repository.ProductsPagingSource
import com.billing.mybilling.database.DatabaseManager
import com.billing.mybilling.database.TempCart
import com.billing.mybilling.utils.AnalyticsType
import com.billing.mybilling.utils.toLoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository,val databaseManager: DatabaseManager):BaseViewModel() {


    var analyticsType = MutableLiveData(Calendar.getInstance().time)
    val instance: Calendar = Calendar.getInstance()
    var filterValue = MutableLiveData(AnalyticsType.DayByDay.type)
    var selectedUser:Users? = null

    var pendingOrders:PendingOrders? = null
    val isPaymentOnline = MutableLiveData(pendingOrders?.payment_type!=1)

    fun updatePaymentType(type:Boolean){
        val paymentType = if(type) 0 else 1
        pendingOrders?.payment_type = paymentType
        isPaymentOnline.postValue(type)
    }

    val totalAmount = MutableLiveData(0)

    fun updateTotalAmount(amount:Int){
        totalAmount.postValue(amount)
    }

    var discount = 0


    fun getPagingProducts()= Pager(PagingConfig(
        pageSize = 10,
        prefetchDistance = 20
    ), pagingSourceFactory = {ProductsPagingSource(homeRepository.homeApiService)}).flow

//    suspend fun getCategoryList()  {
//        homeRepository.getCategories()
//    }



//    suspend fun getVariantList(commonRequestModel: CommonRequestModel)  {
//        homeRepository.getVariants(commonRequestModel)
//    }



//    fun getProductsList(){
//        CoroutineScope(Dispatchers.IO).launch {
//            homeRepository.getProducts()
//        }
//    }


    fun getVariants(commonRequestModel: CommonRequestModel) = liveData(Dispatchers.IO){
        homeRepository.getVariants(commonRequestModel).toLoadingState().catch {  }.collect{
            emit(it)
        }
    }

    fun getCategoriesList(commonRequestModel: CommonRequestModel) = liveData(Dispatchers.IO){
        homeRepository.getCategories(commonRequestModel).toLoadingState().catch {  }.collect{

            emit(it)
        }
    }

    fun addCategory(type: String,commonRequestModel: CommonRequestModel) = liveData(Dispatchers.IO) {
        homeRepository.addCategory(type,commonRequestModel).toLoadingState().catch {  }.collect{
            emit(it)
        }
    }

    fun getStaffList(commonRequestModel: CommonRequestModel) = liveData(Dispatchers.IO) {
        homeRepository.getStaffList(commonRequestModel).toLoadingState().catch {  }.collect{
            emit(it)
        }
    }

    fun addProduct(type: String,addProductRequestModel: AddProductRequestModel) = liveData(Dispatchers.IO) {
        homeRepository.addProduct(type,addProductRequestModel).toLoadingState().catch {  }.collect{
            emit(it)
        }
    }

    fun loginUser(loginRequestModel: LoginRequestModel) = liveData(Dispatchers.IO) {
        homeRepository.loginUser(loginRequestModel).toLoadingState().catch {  }.collect{
            emit(it)
        }
    }

    fun addVariant(type: String,addVariantRequestModel: AddVariantRequestModel) = liveData(Dispatchers.IO) {
        homeRepository.addVariant(type,addVariantRequestModel).toLoadingState().catch {  }.collect{
            emit(it)
        }
    }

    fun addToCart(cartRequestModel: CartRequestModel) = liveData(Dispatchers.IO) {
        homeRepository.addToCart(cartRequestModel).toLoadingState().catch {  }.collect{
            emit(it)
        }
    }

    fun createOrder(addOrderModel: AddOrderModel) = liveData(Dispatchers.IO) {
        homeRepository.createOrder(addOrderModel).toLoadingState().catch {  }.collect{
            emit(it)
        }
    }

    fun getPendingOrders(commonRequestModel: CommonRequestModel) = liveData(Dispatchers.IO) {
        homeRepository.getPendingOrders(commonRequestModel).toLoadingState().catch {  }.collect{
            emit(it)
        }
    }

    suspend fun getCompletedOrders(commonRequestModel: CommonRequestModel)  {
        homeRepository.getCompletedOrders(commonRequestModel)
    }

    fun updatePendingOrder(pendingOrders: PendingOrders?) = liveData(Dispatchers.IO) {
        homeRepository.updatePendingOrder(pendingOrders).toLoadingState().catch {  }.collect{
            emit(it)
        }
    }

    fun getPendingOrderProducts(commonRequestModel: CommonRequestModel) = liveData(Dispatchers.IO) {
        homeRepository.getPendingOrderProducts(commonRequestModel).toLoadingState().catch {  }.collect{
            emit(it)
        }
    }

    fun updateOrderProduct(type:String,products: Products) = liveData(Dispatchers.IO) {
        homeRepository.updateOrderProduct(type,products).toLoadingState().catch {  }.collect{
            emit(it)
        }
    }

    fun updateUsers(type:String,users: Users?) = liveData(Dispatchers.IO) {
        homeRepository.updateUsers(type,users).toLoadingState().catch {  }.collect{
            emit(it)
        }
    }

    fun getProductsList(commonRequestModel: CommonRequestModel) = liveData(Dispatchers.IO)  {
        homeRepository.getProducts(commonRequestModel).toLoadingState().catch {  }.collect{
            emit(it)
        }
    }

    fun getSearchProduct(searchRequestModel: SearchRequestModel) = liveData(Dispatchers.IO) {
        homeRepository.getSearchProducts(searchRequestModel).toLoadingState().catch {  }.collect{
            emit(it)
        }
    }

    fun updateProducts(list:List<TempCart>){
        for(i in list){
            CoroutineScope(Dispatchers.IO).launch {
                databaseManager.updateProduct(i.product)
            }
        }

    }

    fun setFilterData(date:Date):String{
        return when(filterValue.value) {
            AnalyticsType.DayByDay.type-> SimpleDateFormat("dd-MMM-yyyy").format(date)
            AnalyticsType.MonthByMonth.type-> SimpleDateFormat("MMM-YYYY").format(date)
            AnalyticsType.YearByYear.type-> SimpleDateFormat("YYYY").format(date)
            else-> SimpleDateFormat("dd-MMM-yyyy").format(date)
        }
    }

    fun decrementCounter() {
        filterValue.value?.let {
            when(it)
            {
                AnalyticsType.DayByDay.type->instance.add(Calendar.DATE, -1)
                AnalyticsType.MonthByMonth.type->instance.add(Calendar.MONTH, -1)
                AnalyticsType.YearByYear.type->instance.add(Calendar.YEAR, -1)
            }
        }
        analyticsType.value = instance.time
    }

    fun incrementCounter() {
        filterValue.value?.let {
            when(it)
            {
                AnalyticsType.DayByDay.type->instance.add(Calendar.DATE, 1)
                AnalyticsType.MonthByMonth.type->instance.add(Calendar.MONTH, 1)
                AnalyticsType.YearByYear.type->instance.add(Calendar.YEAR, 1)
            }
        }

        analyticsType.value = instance.time

    }

}
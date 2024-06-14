package com.billing.mybilling.presentation

import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.util.Base64
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
import com.billing.mybilling.utils.LoadingState
import com.billing.mybilling.utils.PaymentType
import com.billing.mybilling.utils.toLoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository,val databaseManager: DatabaseManager):BaseViewModel() {

    val yearName = arrayOf("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec")
    var analyticsType = MutableLiveData(Calendar.getInstance().time)
    val instance: Calendar = Calendar.getInstance()
    var filterValue = MutableLiveData(AnalyticsType.DayByDay.type)
    var selectedUser:Users? = null
    val loadState = MutableLiveData<LoadingState>(LoadingState.Loading)

    var pendingOrders:PendingOrders? = null
    val isPaymentOnline = MutableLiveData(pendingOrders?.payment_type!=1)
    var finalAmount = "0"

    fun updatePaymentType(type:Boolean){
        val paymentType = if(type) 0 else 1
        pendingOrders?.payment_type = paymentType
        isPaymentOnline.postValue(type)

        if(paymentType==PaymentType.ONLINE.type){
            pendingOrders?.receiveOnline = finalAmount
            pendingOrders?.receiveCash = "0"
        }else{
            pendingOrders?.receiveOnline = "0"
            pendingOrders?.receiveCash = finalAmount
        }
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


    fun getBusinessDetail(commonRequestModel: CommonRequestModel) = liveData(Dispatchers.IO){
        homeRepository.getBusinessDetail(commonRequestModel).toLoadingState().catch {  }.collect{
            emit(it)
        }
    }

    fun getCategoriesList(commonRequestModel: CommonRequestModel) = liveData(Dispatchers.IO){
        homeRepository.getCategories(commonRequestModel).toLoadingState().catch {  }.collect{
            emit(it)
        }
    }

    suspend fun getCategoriesListFromDatabase(commonRequestModel: CommonRequestModel){
        homeRepository.getCategoriesFromDataBase(commonRequestModel)
    }

    fun addCategory(type: String,addCategoryRequestModel: AddCategoryRequestModel) = liveData(Dispatchers.IO) {
        homeRepository.addCategory(type,addCategoryRequestModel).toLoadingState().catch {  }.collect{
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


    fun createMyOrder(addOrderModel: AddOrderModel) = liveData(Dispatchers.IO) {
        homeRepository.createMyOrder(addOrderModel).toLoadingState().catch {  }.collect{
            emit(it)
        }
    }

    fun readTable(commonRequestModel: CommonRequestModel) = liveData(Dispatchers.IO) {
        homeRepository.readTable(commonRequestModel).toLoadingState().catch {  }.collect{
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

    suspend fun getSingleStaffAttendance(commonRequestModel: CommonRequestModel)  {
        homeRepository.getSingleStaffAttendance(commonRequestModel)
    }

    fun updatePendingOrder(type: String,pendingOrders: PendingOrders?) = liveData(Dispatchers.IO) {
        homeRepository.updatePendingOrder(type,pendingOrders).toLoadingState().catch {  }.collect{
            emit(it)
        }
    }

    fun completeOrder(pendingOrders: PendingOrders?) = liveData(Dispatchers.IO) {
        homeRepository.completeOrder(pendingOrders).toLoadingState().catch {  }.collect{
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

    fun readStaffAttendance(commonRequestModel: CommonRequestModel) = liveData(Dispatchers.IO) {
        homeRepository.readStaffAttendance(commonRequestModel).toLoadingState().catch {  }.collect{
            emit(it)
        }
    }

    fun manageStaffAttendance(type:String,attendanceModel: StaffAttendanceModel?) = liveData(Dispatchers.IO) {
        homeRepository.manageStaffAttendance(type,attendanceModel).toLoadingState().catch {  }.collect{
            emit(it)
        }
    }

    suspend fun getProductsFromDatabase(commonRequestModel: CommonRequestModel){
        homeRepository.getProductsFromDatabase(commonRequestModel)
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


    fun getProductByCategory(searchRequestModel: SearchRequestModel) = liveData(Dispatchers.IO) {
        homeRepository.getProductsByCategory(searchRequestModel).toLoadingState().catch {
            loadState.postValue(LoadingState.Error(it))
        }.collect{

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
            when(it) {
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

    fun BitMapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun createBill(companyName: String, contactNumber: String, items: List<String>): String {
        val builder = StringBuilder()

        // Add company name and contact number
        builder.appendLine("Company Name: $companyName")
        builder.appendLine("Contact Number: $contactNumber")
        builder.appendLine()

        // Add items
        builder.appendLine("Items:")
        for ((index, item) in items.withIndex()) {
            builder.appendLine("${index + 1}. $item")
        }
        builder.appendLine()

        // Add closing message
        builder.appendLine("Thanks for visiting us!")

        return builder.toString()
    }

}
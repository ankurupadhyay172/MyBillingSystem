package com.billing.mybilling.data.repository

import com.billing.mybilling.data.model.request.*
import com.billing.mybilling.data.model.response.CategoryResponseModel
import com.billing.mybilling.data.model.response.*
import com.billing.mybilling.data.remote.HomeApiService
import com.billing.mybilling.database.DatabaseManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepository @Inject constructor(val homeApiService: HomeApiService,val databaseManager: DatabaseManager) {

    suspend fun getStaffList(commonRequestModel: CommonRequestModel):Flow<StaffResponseModel> = flow {
        val response = homeApiService.getStaffList(commonRequestModel)
        emit(response)
    }

    suspend fun addCategory(type:String,commonRequestModel: CommonRequestModel):Flow<CommonResponseModel> = flow {
        val response = homeApiService.addCategory(type,commonRequestModel)
        emit(response)
    }

    suspend fun addProduct(type: String,addProductRequestModel: AddProductRequestModel):Flow<CommonResponseModel> = flow {
        val response = homeApiService.addProduct(type,addProductRequestModel)
        emit(response)
    }

    suspend fun loginUser(loginRequestModel: LoginRequestModel):Flow<LoginResponseModel> = flow {
        val response = homeApiService.loginUser(loginRequestModel)
        emit(response)
    }

    suspend fun addVariant(type: String,addVariantRequestModel: AddVariantRequestModel):Flow<CommonResponseModel> = flow {
        val response = homeApiService.addVariant(type,addVariantRequestModel)
        emit(response)
    }

//    suspend fun getCategories() {
//        val response = homeApiService.getCategories()
//        if (response.status==1)
//            databaseManager.addCategory(response.result)
//    }

    suspend fun getCategories(commonRequestModel: CommonRequestModel):Flow<CategoryResponseModel> = flow {
        val response = homeApiService.getCategories(commonRequestModel)
        emit(response)
    }

    suspend fun getVariants(commonRequestModel: CommonRequestModel):Flow<VariantsResponseModel> = flow {
        val response = homeApiService.getVariants(commonRequestModel)
        emit(response)
    }


    suspend fun getProducts(commonRequestModel: CommonRequestModel):Flow<ProductsResponseModel> = flow {
        val response = homeApiService.getProducts(commonRequestModel)
        emit(response)

    }

    suspend fun getSearchProducts(searchRequestModel: SearchRequestModel):Flow<ProductsResponseModel> = flow {
        val response = homeApiService.getSearchProducts(searchRequestModel)
        emit(response)
    }

    suspend fun addToCart(cartRequestModel: CartRequestModel):Flow<CommonResponseModel> = flow {
        val response = homeApiService.addToCart(cartRequestModel)
        emit(response)
    }

    suspend fun createOrder(addOrderModel: AddOrderModel):Flow<CommonResponseModel> = flow {
        val response = homeApiService.createOrder(addOrderModel)
        emit(response)
    }

    suspend fun getPendingOrders(commonRequestModel: CommonRequestModel):Flow<PendingOrdersResponseModel> = flow {
        val response = homeApiService.getPendingOrders(commonRequestModel)
        emit(response)
    }

    suspend fun getCompletedOrders(commonRequestModel: CommonRequestModel){
        val response = homeApiService.getCompletedOrders(commonRequestModel)
        if (response.status==1)
        databaseManager.addDelivered(response.result)
    }

    suspend fun updatePendingOrder(pendingOrders: PendingOrders?):Flow<CommonResponseModel> = flow {
        val response = homeApiService.updatePendingOrder(pendingOrders)
        emit(response)
    }

    suspend fun getPendingOrderProducts(commonRequestModel: CommonRequestModel):Flow<PendingOrderDetailsResponseModel> = flow {
        val response = homeApiService.getPendingOrdersProducts(commonRequestModel)
        emit(response)
    }

    suspend fun updateOrderProduct(type:String,products: Products):Flow<CommonResponseModel> = flow {
        val response = homeApiService.updateOrderProduct(type,products)
        emit(response)
    }

    suspend fun updateUsers(type:String,users: Users?):Flow<CommonResponseModel> = flow {
        val response = homeApiService.updateUsers(type,users)
        emit(response)
    }
}
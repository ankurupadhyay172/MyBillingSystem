package com.billing.mybilling.data.remote

import com.billing.mybilling.data.model.request.*
import com.billing.mybilling.data.model.response.CategoryResponseModel
import com.billing.mybilling.data.model.response.*
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface HomeApiService {

    @POST("ReadStaff.php")
    suspend fun getStaffList(@Body commonRequestModel: CommonRequestModel):StaffResponseModel

    @POST("AddCategory.php")
    suspend fun addCategory(@Query("type") type:String,@Body addCategoryRequestModel: AddCategoryRequestModel): CommonResponseModel

    @POST("AddProduct.php")
    suspend fun addProduct(@Query("type") type:String,@Body addProductRequestModel: AddProductRequestModel): CommonResponseModel

    @POST("LoginUser.php")
    suspend fun loginUser(@Body loginRequestModel: LoginRequestModel): LoginResponseModel

    @POST("AddVariant.php")
    suspend fun addVariant(@Query("type") type:String,@Body addVariantRequestModel: AddVariantRequestModel): CommonResponseModel

    @POST("ReadCategories.php")
    suspend fun getCategories(@Body commonRequestModel: CommonRequestModel): CategoryResponseModel

    @POST("ReadVariants.php")
    suspend fun getVariants(@Body commonRequestModel: CommonRequestModel): VariantsResponseModel

    @POST("GetProductsPagination.php")
    suspend fun getAllProductsByPagination(@Query("page") page:Int): ProductsResponseModel

    @POST("ReadProducts.php")
    suspend fun getProducts(@Body commonRequestModel: CommonRequestModel): ProductsResponseModel

    @POST("ReadSearchProducts.php")
    suspend fun getSearchProducts(@Body searchRequestModel: SearchRequestModel): ProductsResponseModel

    @POST("ReadProductByCategory.php")
    suspend fun getProductsByCategory(@Body searchRequestModel: SearchRequestModel): ProductsResponseModel

    @POST("AddToCart.php")
    suspend fun addToCart(cartRequestModel: CartRequestModel): CommonResponseModel

    @POST("CreateOrder.php")
    suspend fun createMyOrder(@Body addOrderModel: AddOrderModel): OrderResponseModel

    @POST("ReadTable.php")
    suspend fun readTable(@Body commonRequestModel: CommonRequestModel): TableResponseModel

    @POST("ReadPendingOrders.php")
    suspend fun getPendingOrders(@Body commonRequestModel: CommonRequestModel):PendingOrdersResponseModel

    @POST("ReadCompletedOrders.php")
    suspend fun getCompletedOrders(@Body commonRequestModel: CommonRequestModel):PendingOrdersResponseModel

    @POST("UpdatePendingOrder.php")
    suspend fun updatePendingOrder(@Query("type") type:String,@Body pendingOrders: PendingOrders?):CommonResponseModel

    @POST("ReadPendingOrdersDetails.php")
    suspend fun getPendingOrdersProducts(@Body commonRequestModel: CommonRequestModel):PendingOrderDetailsResponseModel

    @POST("UpdateOrderProduct.php")
    suspend fun updateOrderProduct(@Query("type") type:String,@Body products: Products): CommonResponseModel

    @POST("UpdateUsers.php")
    suspend fun updateUsers(@Query("type") type:String,@Body user: Users?): CommonResponseModel
}
package com.billing.mybilling.hilt


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.billing.mybilling.BuildConfig
import com.billing.mybilling.data.remote.HomeApiService
import com.billing.mybilling.presentation.login.LoginApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    fun provideBaseUrl(): String = BuildConfig.BASE_URL

    //create logger
    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)


    //create okhttp client
    val okhttp = OkHttpClient.Builder().addInterceptor(logger)

    @Provides
    @Singleton
    fun provideRetrofitBuilder(baseurl:String): Retrofit = Retrofit.Builder().baseUrl(baseurl)
        .addConverterFactory(GsonConverterFactory.create()).client(okhttp.connectTimeout(60,TimeUnit.SECONDS).writeTimeout(60,TimeUnit.SECONDS).build()).build()


    @Provides
    fun provideHomeApiService(retrofit: Retrofit): HomeApiService = retrofit.create(HomeApiService::class.java)

    @Provides
    fun provideLoginApiService(retrofit: Retrofit): LoginApiService = retrofit.create(LoginApiService::class.java)

}
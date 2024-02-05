package com.billing.mybilling.hilt

import com.billing.mybilling.presentation.login.LoginRepository
import com.billing.mybilling.presentation.login.LoginRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule{

    @Singleton
    @Binds
    abstract fun provideRepository(impl: LoginRepositoryImpl):LoginRepository
}
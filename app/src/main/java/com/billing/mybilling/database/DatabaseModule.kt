package com.billing.mybilling.database

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {

    @Singleton
    @Binds
    abstract fun provideDbManager(impl:DatabaseManagerImpl): DatabaseManager
}
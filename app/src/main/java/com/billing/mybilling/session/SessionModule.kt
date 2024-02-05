package com.billing.mybilling.session

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SessionModule {
    @Singleton
    @Binds
    abstract fun provideSessionManager(impl: SessionManagerImpl):SessionManager
}
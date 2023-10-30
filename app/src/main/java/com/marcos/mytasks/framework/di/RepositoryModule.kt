package com.marcos.mytasks.framework.di

import com.marcos.mytasks.data.repository.AuthRepository
import com.marcos.mytasks.data.repository.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsAuthRepository(repository: AuthRepositoryImpl) : AuthRepository
}
package com.marcos.mytasks.framework.di

import com.marcos.mytasks.data.dataSource.AuthRemoteDataSource
import com.marcos.mytasks.data.dataSource.AuthRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    fun bindsAuthRemoteDataSource(dataSource: AuthRemoteDataSourceImpl) : AuthRemoteDataSource
}
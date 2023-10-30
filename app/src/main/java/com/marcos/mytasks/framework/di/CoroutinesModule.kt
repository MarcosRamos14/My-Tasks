package com.marcos.mytasks.framework.di

import com.marcos.mytasks.domain.usecase.base.AppCoroutinesDispatchers
import com.marcos.mytasks.domain.usecase.base.CoroutinesDispatchers
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CoroutinesModule {

    @Binds
    fun bindsDispatchers(dispatchers: AppCoroutinesDispatchers) : CoroutinesDispatchers
}
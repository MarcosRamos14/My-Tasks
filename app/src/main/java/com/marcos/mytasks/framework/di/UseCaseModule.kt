package com.marcos.mytasks.framework.di

import com.marcos.mytasks.domain.usecase.GetLoginUserUseCase
import com.marcos.mytasks.domain.usecase.GetLoginUserUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {

    @Binds
    fun bindsGetLoginUserUseCase(useCase: GetLoginUserUseCaseImpl) : GetLoginUserUseCase
}
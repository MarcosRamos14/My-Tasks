package com.marcos.mytasks.framework.di

import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.marcos.mytasks.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun providesGoogleSignInOptions() : GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(BuildConfig.WEB_CLIENT_ID)
            .build()
    }
}
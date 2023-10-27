package com.marcos.mytasks.data.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.marcos.mytasks.data.dataSource.AuthRemoteDataSource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun loginUser(email: String, password: String): AuthResult {
        return authRemoteDataSource.loginUser(email, password)
    }

    override suspend fun signInGoogle(credential: AuthCredential): AuthResult {
        return authRemoteDataSource.signInGoogle(credential)
    }

    override suspend fun signOut() {
        return authRemoteDataSource.signOut()
    }
}
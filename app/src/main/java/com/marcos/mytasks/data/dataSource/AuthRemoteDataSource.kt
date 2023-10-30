package com.marcos.mytasks.data.dataSource

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult

interface AuthRemoteDataSource {

    suspend fun loginUser(email: String, password: String) : AuthResult

    suspend fun signInGoogle(credential: AuthCredential) : AuthResult

    suspend fun signOut()

    suspend fun recoverAccount(email: String)
}
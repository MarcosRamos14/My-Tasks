package com.marcos.mytasks.data.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult

interface AuthRepository {

    suspend fun loginUser(email: String, password: String) : AuthResult

    suspend fun signInGoogle(credential: AuthCredential) : AuthResult

    suspend fun signOut()

    suspend fun recoverAccount(email: String)
}
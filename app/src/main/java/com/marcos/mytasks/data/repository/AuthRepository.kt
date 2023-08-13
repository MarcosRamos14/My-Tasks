package com.marcos.mytasks.data.repository

import com.google.firebase.auth.AuthResult

interface AuthRepository {

    suspend fun loginUser(email: String, password: String) : AuthResult
}
package com.marcos.mytasks.data.dataSource

import com.google.firebase.auth.AuthResult

interface AuthRemoteDataSource {

    suspend fun loginUser(email: String, password: String) : AuthResult
}
package com.marcos.mytasks.data.dataSource

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRemoteDataSource {

    override suspend fun loginUser(email: String, password: String): AuthResult {
        return firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signInGoogle(credential: AuthCredential): AuthResult {
        return firebaseAuth.signInWithCredential(credential).await()
    }

    override suspend fun signOut() {
        return firebaseAuth.signOut()
    }

    override suspend fun recoverAccount(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
    }
}
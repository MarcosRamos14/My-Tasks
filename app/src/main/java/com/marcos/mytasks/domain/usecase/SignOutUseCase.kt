package com.marcos.mytasks.domain.usecase

import com.marcos.mytasks.data.repository.AuthRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

interface SignOutUseCase : suspend () -> Unit

class SignOutUseCaseImpl @Inject constructor(
    private val repository: AuthRepository
) : SignOutUseCase {

    override suspend fun invoke() {
        try {
            repository.signOut()
        } catch (exception: Exception) {
            exception.printStackTrace()
            if (exception is CancellationException) throw exception
        }
    }
}
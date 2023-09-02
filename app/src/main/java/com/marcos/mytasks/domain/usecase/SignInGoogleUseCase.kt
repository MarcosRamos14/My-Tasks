package com.marcos.mytasks.domain.usecase

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.marcos.mytasks.data.repository.AuthRepository
import com.marcos.mytasks.domain.usecase.SignInGoogleUseCase.Params
import com.marcos.mytasks.domain.usecase.base.CoroutinesDispatchers
import com.marcos.mytasks.domain.usecase.base.ResultStatus
import com.marcos.mytasks.domain.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface SignInGoogleUseCase {
    operator fun invoke(params: Params) : Flow<ResultStatus<AuthResult>>
    data class Params(val credential: AuthCredential)
}

class SignInGoogleUseCaseImpl @Inject constructor(
    private val repository: AuthRepository,
    private val dispatchers: CoroutinesDispatchers
) : UseCase<Params, AuthResult>(), SignInGoogleUseCase {

    override suspend fun doWork(params: Params): ResultStatus<AuthResult> {
        return withContext(dispatchers.io()) {
            val signInGoogle = repository.signInGoogle(params.credential)
            ResultStatus.Success(signInGoogle)
        }
    }
}
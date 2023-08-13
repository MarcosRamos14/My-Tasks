package com.marcos.mytasks.domain.usecase

import com.google.firebase.auth.AuthResult
import com.marcos.mytasks.data.repository.AuthRepository
import com.marcos.mytasks.domain.usecase.GetLoginUserUseCase.Params
import com.marcos.mytasks.domain.usecase.base.CoroutinesDispatchers
import com.marcos.mytasks.domain.usecase.base.ResultStatus
import com.marcos.mytasks.domain.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetLoginUserUseCase {

    operator fun invoke(params: Params) : Flow<ResultStatus<AuthResult>>

    data class Params(val email: String, val password: String)
}

class GetLoginUserUseCaseImpl @Inject constructor(
    private val repository: AuthRepository,
    private val dispatchers: CoroutinesDispatchers
) : UseCase<Params, AuthResult>(), GetLoginUserUseCase {

    override suspend fun doWork(params: Params): ResultStatus<AuthResult> {
        return withContext(dispatchers.io()) {
            val user = repository.loginUser(params.email, params.password)
            ResultStatus.Success(user)
        }
    }
}
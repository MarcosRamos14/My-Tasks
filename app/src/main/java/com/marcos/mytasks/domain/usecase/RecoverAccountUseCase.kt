package com.marcos.mytasks.domain.usecase

import com.marcos.mytasks.data.repository.AuthRepository
import com.marcos.mytasks.domain.usecase.RecoverAccountUseCase.Params
import com.marcos.mytasks.domain.usecase.base.CoroutinesDispatchers
import com.marcos.mytasks.domain.usecase.base.ResultStatus
import com.marcos.mytasks.domain.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface RecoverAccountUseCase {
    operator fun invoke(params: Params) : Flow<ResultStatus<Unit>>
    data class Params(val email: String)
}

class RecoverAccountUseCaseImpl @Inject constructor(
    private val repository: AuthRepository,
    private val dispatchers: CoroutinesDispatchers
) : UseCase<Params, Unit>(), RecoverAccountUseCase {

    override suspend fun doWork(params: Params): ResultStatus<Unit> {
        return withContext(dispatchers.io()) {
            val email = repository.recoverAccount(params.email)
            ResultStatus.Success(email)
        }
    }
}
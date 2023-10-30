package com.marcos.mytasks.ui.authorization.recoverAccount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.marcos.mytasks.domain.usecase.RecoverAccountUseCase
import com.marcos.mytasks.domain.usecase.base.CoroutinesDispatchers
import com.marcos.mytasks.framework.firebase.FirebaseHelper
import com.marcos.mytasks.ui.extension.watchStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecoverAccountViewModel @Inject constructor(
    private val dispatchers: CoroutinesDispatchers,
    private val recoverAccountUseCase: RecoverAccountUseCase
) : ViewModel() {

    private val action = MutableLiveData<Action>()
    val state: LiveData<UiState> = action.switchMap {
        liveData(dispatchers.io()) {
            when (it) {
                is Action.RecoverAccount -> {
                    recoverAccountUseCase.invoke(
                        RecoverAccountUseCase.Params(it.email)
                    ).watchStatus(
                        success = {
                            emit(UiState.Success(successMessage = FirebaseHelper.validSuccess()))
                        },
                        error = { error ->
                            emit(
                                UiState.Error(
                                    errorMessage = FirebaseHelper.validError(error.message ?: "")
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    fun recoverAccount(email: String) {
        action.value = Action.RecoverAccount(email)
    }

    sealed class UiState {
        data class Success(val successMessage: Int) : UiState()
        data class Error(val errorMessage: Int) : UiState()
    }

    sealed class Action {
        data class RecoverAccount(val email: String) : Action()
    }
}
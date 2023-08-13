package com.marcos.mytasks.ui.authorization.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.google.firebase.auth.AuthResult
import com.marcos.mytasks.domain.usecase.GetLoginUserUseCase
import com.marcos.mytasks.domain.usecase.base.CoroutinesDispatchers
import com.marcos.mytasks.framework.firebase.FirebaseHelper
import com.marcos.mytasks.ui.extension.watchStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dispatchers: CoroutinesDispatchers,
    private val getLoginUserUseCase: GetLoginUserUseCase
) : ViewModel() {

    private val action = MutableLiveData<Action>()
    val state: LiveData<UiState> = action.switchMap {
        liveData(dispatchers.io()) {
            when (it) {
                is Action.LoginUser -> {
                    getLoginUserUseCase.invoke(
                        GetLoginUserUseCase.Params(it.email, it.password)
                    ).watchStatus(
                        success = { user -> emit(UiState.Success(user)) },
                        error = { error ->
                            emit(UiState.Error(
                                    errorMessage = FirebaseHelper.validError(error.message ?: "")
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    fun loginUser(email: String, password: String) {
        action.value = Action.LoginUser(email, password)
    }

    sealed class UiState {
        data class Success(val user: AuthResult) : UiState()
        data class Error(val errorMessage: Int) : UiState()
    }

    sealed class Action {
        data class LoginUser(val email: String, val password: String) : Action()
    }
}
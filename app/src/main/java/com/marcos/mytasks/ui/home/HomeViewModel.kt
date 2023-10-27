package com.marcos.mytasks.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.marcos.mytasks.domain.usecase.SignOutUseCase
import com.marcos.mytasks.domain.usecase.base.CoroutinesDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dispatchers: CoroutinesDispatchers,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val action = MutableLiveData<Action>()
    val state: LiveData<UiState> = action.switchMap {
        liveData(dispatchers.io()) {
            when (it) {
                Action.SignOutGoogle -> {
                    signOutUseCase
                    emit(UiState.Success)
                }
            }
        }
    }

    fun signOutGoogle() {
        action.value = Action.SignOutGoogle
    }

    sealed class UiState {
        object Success : UiState()
    }

    sealed class Action {
        object SignOutGoogle : Action()
    }
}
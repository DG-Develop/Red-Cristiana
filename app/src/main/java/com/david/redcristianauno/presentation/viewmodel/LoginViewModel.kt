package com.david.redcristianauno.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.david.redcristianauno.domain.usecases.*
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val currentUserUseCase: CurrentUserUseCase
) : ViewModel() {

    fun login(email: String, password: String) =
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.Loading())

            try {
                emit(loginUserUseCase.invoke(email, password))
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }

    fun isCurrentUserFromFirebase(): Boolean{
        var isCurrent = false
        viewModelScope.launch {
             isCurrent= currentUserUseCase.invoke()
        }
        return isCurrent
    }
}
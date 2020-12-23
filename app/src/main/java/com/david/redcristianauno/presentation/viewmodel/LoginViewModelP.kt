package com.david.redcristianauno.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.domain.usecases.CreateUserUseCase
import com.david.redcristianauno.domain.usecases.CurrentUserUseCase
import com.david.redcristianauno.domain.usecases.GetUserByIdUseCase
import com.david.redcristianauno.domain.usecases.LoginUserUseCase
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModelP @ViewModelInject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val currentUserUseCase: CurrentUserUseCase
) : ViewModel() {

    private val userData = MutableLiveData<String>()

    fun getUser(userId: String){
        userData.value = userId
    }


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


    val getUser = userData.switchMap { userId ->
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.Loading())
            try {
                emit(getUserByIdUseCase.invokeRemote(userId))
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }
    }

    fun saveUserLocal(data: User){
        viewModelScope.launch {
            createUserUseCase.invokeLocal(data)
        }
    }
}
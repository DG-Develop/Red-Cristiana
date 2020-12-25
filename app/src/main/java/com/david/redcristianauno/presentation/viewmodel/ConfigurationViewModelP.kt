package com.david.redcristianauno.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.david.redcristianauno.domain.usecases.GetIdUserUseCase
import com.david.redcristianauno.domain.usecases.GetUserByIdUseCase
import com.david.redcristianauno.domain.usecases.SignOutUseCase
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ConfigurationViewModelP @ViewModelInject constructor(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getIdUserUseCase: GetIdUserUseCase,
    private val signOutUseCase: SignOutUseCase
): ViewModel(){

    private val userData = MutableLiveData<String>()

    fun getUser(userId: String){
        userData.value = userId
    }

    val fetchUser = userData.switchMap { userId ->
        liveData(viewModelScope.coroutineContext + Dispatchers.IO){
            emit(Resource.Loading())

            try {
                emit(getUserByIdUseCase.invokeLocal(userId))
            }catch (e: Exception){
                emit(Resource.Failure(e))
            }
        }
    }

    fun getIdUser(): String{
        var result = ""
        viewModelScope.launch {
            result = getIdUserUseCase.invoke().toString()
        }
        return  result
    }

    fun signOutFromFirebase(){
        viewModelScope.launch {
            signOutUseCase.invoke()
        }
    }
}
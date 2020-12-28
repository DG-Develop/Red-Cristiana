package com.david.redcristianauno.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.david.redcristianauno.domain.usecases.GetIdUserUseCase
import com.david.redcristianauno.domain.usecases.GetUserCached
import com.david.redcristianauno.domain.usecases.SignOutUseCase
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception

class ConfigurationViewModelP @ViewModelInject constructor(
    private val getUserCached: GetUserCached,
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
                emit(getUserCached.invoke(userId))
            }catch (e: Exception){
                emit(Resource.Failure(e))
            }
        }
    }

   /* fun fetchUser() = liveData(viewModelScope.coroutineContext + Dispatchers.IO){
        emit(Resource.Loading())
        try {
            getUserCached.invoke().collect { user->
                emit(user)
            }
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }*/

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
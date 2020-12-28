package com.david.redcristianauno.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.david.redcristianauno.domain.usecases.GetIdUserUseCase
import com.david.redcristianauno.domain.usecases.GetUserByIdUseCase
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModelP @ViewModelInject constructor(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getIdUserUseCase: GetIdUserUseCase
) : ViewModel(){

    private val userData = MutableLiveData<String>()

    fun getUser(userId: String){
        userData.value = userId
    }

    fun getIdUserFromFirebase(): String{
        var result=""
        viewModelScope.launch {
            result = getIdUserUseCase.invoke().toString()
        }
        return result
    }

    val fetchUserId = userData.switchMap { userId ->
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.Loading())
            try {
                getUserByIdUseCase.invoke(userId).collect { user->
                    emit(user)
                }
            }catch (e: Exception){
                emit(Resource.Failure(e))
            }
        }
    }
}
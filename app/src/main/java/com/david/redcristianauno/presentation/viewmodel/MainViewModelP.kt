package com.david.redcristianauno.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.david.redcristianauno.domain.usecases.GetUserByIdUseCase
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class MainViewModelP @ViewModelInject constructor(
    private val getUserByIdUseCase: GetUserByIdUseCase
) : ViewModel(){

    private val userData = MutableLiveData<String>()

    fun setSearchUser(userId: String){
        userData.value = userId
    }

    val fetchUserId = userData.switchMap { userId ->
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.Loading())

            try {
                emit(getUserByIdUseCase.invokeRemote(userId))
            }catch (e: Exception){
                emit(Resource.Failure(e))
            }
        }
    }
}
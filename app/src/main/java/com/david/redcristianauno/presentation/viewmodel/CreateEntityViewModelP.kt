package com.david.redcristianauno.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.david.redcristianauno.domain.usecases.GetListUsersUseCase
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class CreateEntityViewModelP @ViewModelInject constructor(
    private val getListUsersUseCase: GetListUsersUseCase
) : ViewModel(){


    fun getListUsersFromFirebase() = liveData(viewModelScope.coroutineContext + Dispatchers.IO){
        emit(Resource.Loading())

        try {
            getListUsersUseCase.invoke().collect { userList ->
                emit(userList)
            }
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }

}
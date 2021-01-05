package com.david.redcristianauno.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.david.redcristianauno.domain.usecases.GetListUsersUseCase
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class CreateEntityViewModel @ViewModelInject constructor(
    private val getListUsersUseCase: GetListUsersUseCase
) : ViewModel(){

    private val filterLiveData = MutableLiveData<List<String>>()

    fun setFilter(filter: List<String>){
        filterLiveData.value = filter
    }

    fun getListUsersFromFirebase() = filterLiveData.switchMap { filter ->
        liveData(viewModelScope.coroutineContext + Dispatchers.IO){
            emit(Resource.Loading())

            try {
                getListUsersUseCase.invoke(filter).collect { userList ->
                    emit(userList)
                }
            }catch (e: Exception){
                emit(Resource.Failure(e))
            }
        }
    }

}
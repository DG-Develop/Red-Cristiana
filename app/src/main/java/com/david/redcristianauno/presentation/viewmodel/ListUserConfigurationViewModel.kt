package com.david.redcristianauno.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.david.redcristianauno.domain.usecases.GetListUsersUseCase
import com.david.redcristianauno.domain.usecases.UpdateUserFirestoreUseCase
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception

class ListUserConfigurationViewModel @ViewModelInject constructor(
    private val getListUsersUseCase: GetListUsersUseCase,
    private val updateUserFirestoreUseCase: UpdateUserFirestoreUseCase
) : ViewModel(){

    private val filterLiveData = MutableLiveData<List<String>>()

    fun setFilter(filter: List<String>){
        filterLiveData.value = filter
    }

    fun getListUserFromFirebase() = filterLiveData.switchMap { filter ->
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.Loading())
            try {
                getListUsersUseCase.invoke(filter).collect { list->
                    emit(list)
                }
            }catch (e: Exception){
                emit(Resource.Failure(e))
            }
        }
    }
    fun updateUserFromFirestore(data: Map<String, String>){
        viewModelScope.launch {
            updateUserFirestoreUseCase.invoke(data)
        }
    }
}
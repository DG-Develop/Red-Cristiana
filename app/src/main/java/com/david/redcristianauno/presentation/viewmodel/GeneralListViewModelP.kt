package com.david.redcristianauno.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.david.redcristianauno.domain.usecases.GetNetworkUseCase
import com.david.redcristianauno.domain.usecases.GetSubNetworkUseCase
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class GeneralListViewModelP @ViewModelInject constructor(
    private val getNetworkUseCase: GetNetworkUseCase,
    private val getSubNetworkUseCase: GetSubNetworkUseCase
): ViewModel() {

    private val churchData = MutableLiveData<String>()
    private val networkData = MutableLiveData<List<String>>()

    fun setChurch(church: String){
        churchData.value = church
    }

    val fetchNetwork = churchData.switchMap { church ->
        liveData(viewModelScope.coroutineContext + Dispatchers.IO){
            emit(Resource.Loading())
            try {
                emit(getNetworkUseCase.invoke(church))
            }catch (e: Exception){
                emit(Resource.Failure(e))
            }
        }
    }

    fun setNetwork(church: String, network: String){
        val list = listOf(church, network)
        networkData.value = list
    }

    val fetchSubNetwork = networkData.switchMap { network ->
        liveData(viewModelScope.coroutineContext + Dispatchers.IO){
            emit(Resource.Loading())

            try {
                emit(getSubNetworkUseCase.invoke(network[0], network[1]))
            }catch (e: Exception){
                emit(Resource.Failure(e))
            }
        }
    }
}
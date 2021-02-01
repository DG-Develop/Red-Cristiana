package com.david.redcristianauno.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.domain.models.UserDataSource
import com.david.redcristianauno.domain.usecases.*
import com.david.redcristianauno.vo.Resource
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class CaptureUserViewModel @ViewModelInject constructor(
    private val createUserAuthUseCase: CreateUserAuthUseCase,
    private val createUserFirestoreUseCase: CreateUserFirestoreUseCase,
    private val getNetworkUseCase: GetNetworkUseCase,
    private val getSubNetworkUseCase: GetSubNetworkUseCase,
    private val getCellsUseCase: GetCellsUseCase,
    private val getPathCellUseCase: GePathCellUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel(){

    private val credentials = MutableLiveData<List<String>>()
    private val churchData = MutableLiveData<String>()
    private val networkData = MutableLiveData<List<String>>()
    private val subNetworkData = MutableLiveData<List<String>>()


    fun setCredentials(email: String, password: String){
        credentials.value = listOf(email, password)
    }

    fun setChurch(church: String){
        churchData.value = church
    }

    fun setNetwork(church: String, network: String){
        val list = listOf(church, network)
        networkData.value = list
    }

    fun setSubNetwork(church: String, network: String, subNetwork: String){
        val list = listOf(church, network, subNetwork)
        subNetworkData.value = list
    }

    fun getPathCellFromFirebase(
        church: String, network: String, subNetwork: String, cell: String
    ): DocumentReference?{
        var result: DocumentReference? = null
        viewModelScope.launch {
            result = getPathCellUseCase.invoke(church, network, subNetwork, cell)
        }
        return result
    }

    fun createUserAuthFromFirebase() = credentials.switchMap { list ->
        liveData(viewModelScope.coroutineContext + Dispatchers.IO){
            emit(Resource.Loading())

            try {
                emit(createUserAuthUseCase.invoke(list[0], list[1]))
            }catch (e: Exception){
                emit(Resource.Failure(e))
            }
        }
    }

    val fetchNetwork = churchData.switchMap { church->
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.Loading())

            try {
                emit(getNetworkUseCase.invoke(church))
            }catch (e: Exception){
                emit(Resource.Failure(e))
            }
        }
    }

    val fetchSubNetwork = networkData.switchMap { network->
        liveData(viewModelScope.coroutineContext + Dispatchers.IO){
            emit(Resource.Loading())

            try {
                emit(getSubNetworkUseCase.invoke(network[0], network[1]))
            }catch (e: Exception){
                emit(Resource.Failure(e))
            }
        }
    }

    val fetchCell = subNetworkData.switchMap { subNetwork ->
        liveData(viewModelScope.coroutineContext + Dispatchers.IO){
            emit(Resource.Loading())

            try {
                emit(getCellsUseCase.invoke(subNetwork[0], subNetwork[1], subNetwork[2]))
            }catch (e: Exception){
                emit(Resource.Failure(e))
            }
        }
    }


    fun createUserFirestoreFromFirebase(user: UserDataSource, callback: Callback<Void>){
        createUserFirestoreUseCase.invoke(user, callback)
    }

    fun signOut(){
        viewModelScope.launch {
            signOutUseCase.invoke()
        }
    }
}
package com.david.redcristianauno.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.domain.models.UserDataSource
import com.david.redcristianauno.domain.usecases.CreateUserAuthUseCase
import com.david.redcristianauno.domain.usecases.CreateUserFirestoreUseCase
import com.david.redcristianauno.domain.usecases.SignOutUseCase
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class RegisterViewModel @ViewModelInject constructor(
    private val createUseAuthUseCase: CreateUserAuthUseCase,
    private val createUserFirestoreUseCase: CreateUserFirestoreUseCase,
    private val signOutUseCase: SignOutUseCase
): ViewModel() {

    private val credentials = MutableLiveData<List<String>>()


    fun setCredentials(email: String, password: String){
        credentials.value = listOf(email, password)
    }


    fun createUserAuthFromFirebase() = credentials.switchMap { list ->
        liveData(viewModelScope.coroutineContext + Dispatchers.IO){
            emit(Resource.Loading())

            try {
                emit(createUseAuthUseCase.invoke(list[0], list[1]))
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
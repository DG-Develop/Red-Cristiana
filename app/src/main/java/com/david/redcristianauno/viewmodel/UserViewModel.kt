package com.david.redcristianauno.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.david.redcristianauno.domain.UserUseCase
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class UserViewModel(userUseCase: UserUseCase): ViewModel() {
    lateinit var id_user:String

    val fetchNameUser = liveData(Dispatchers.IO) {
        emit(Resource.Loading())

        try {
            val userName = userUseCase.getNamesUsers(id_user)
            emit(userName)
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }

}
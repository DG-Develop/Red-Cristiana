package com.david.redcristianauno.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.david.redcristianauno.domain.DataCelulaUseCase
import com.david.redcristianauno.model.network.FirebaseService
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class DataCelulaViewModel(dataCelulaUseCase: DataCelulaUseCase): ViewModel() {
    lateinit var id_user:String
    val firebaseService = FirebaseService()

    val fetchNameUser = liveData(Dispatchers.IO) {
        emit(Resource.Loading())

        try {
            val userName = dataCelulaUseCase.getNamesUsers(id_user)
            emit(userName)
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }

}
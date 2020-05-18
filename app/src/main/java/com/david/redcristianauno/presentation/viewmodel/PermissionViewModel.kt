package com.david.redcristianauno.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.david.redcristianauno.domain.PermissionUseCase
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class PermissionViewModel(permissionUseCase: PermissionUseCase) : ViewModel(){
    private val user = permissionUseCase
    lateinit var id_user:String

    val fetchPermission = liveData(Dispatchers.IO) {
        emit(Resource.Loading())

        try {
            val permission = permissionUseCase.getPermission(id_user)
            emit(permission)
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }
}
package com.david.redcristianauno.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.david.redcristianauno.domain.PermissionUseCase

class PermissionViewModelFactory(private val permissionUseCase: PermissionUseCase): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(PermissionUseCase::class.java).newInstance(permissionUseCase)
    }
}
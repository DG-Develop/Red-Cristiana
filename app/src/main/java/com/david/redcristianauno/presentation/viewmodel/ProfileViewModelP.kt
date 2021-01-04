package com.david.redcristianauno.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.david.redcristianauno.domain.usecases.UpdateUserFirestoreUseCase
import kotlinx.coroutines.launch

class ProfileViewModelP @ViewModelInject constructor(
    private val updateUserFirestoreUseCase: UpdateUserFirestoreUseCase
) : ViewModel() {


    fun updateUser(fields: Map<String, String>){
        viewModelScope.launch {
            updateUserFirestoreUseCase.invoke(fields)
        }
    }
}
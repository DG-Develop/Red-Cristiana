package com.david.redcristianauno.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.david.redcristianauno.domain.usecases.UpdateUserFirestoreUseCase
import kotlinx.coroutines.launch

class ProfileViewModel @ViewModelInject constructor(
    private val updateUserFirestoreUseCase: UpdateUserFirestoreUseCase
) : ViewModel() {


    fun updateUser(fields: Map<String, String>, id: String){
        viewModelScope.launch {
            updateUserFirestoreUseCase.invoke(fields, id)
        }
    }
}
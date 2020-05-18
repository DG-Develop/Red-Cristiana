package com.david.redcristianauno.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.david.redcristianauno.domain.ProfileUseCase

class ProfileViewModelFactory (private val profileUseCase: ProfileUseCase): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ProfileUseCase::class.java).newInstance(profileUseCase)
    }
}
package com.david.redcristianauno.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.david.redcristianauno.domain.ChurchUseCase

class CreateEntityViewModelFactory (private val churchUseCase: ChurchUseCase): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ChurchUseCase::class.java).newInstance(churchUseCase)
    }
}
package com.david.redcristianauno.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.david.redcristianauno.domain.ConfigurationUseCase

class ConfigurationViewModelFactory(private val config: ConfigurationUseCase): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ConfigurationUseCase::class.java).newInstance(config)
    }
}
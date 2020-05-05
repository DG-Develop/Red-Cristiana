package com.david.redcristianauno.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.david.redcristianauno.domain.DataCelulaUseCase

class DataCelulaViewModelFactory(private val dataCelulaUseCase: DataCelulaUseCase): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(DataCelulaUseCase::class.java).newInstance(dataCelulaUseCase)
    }
}
package com.david.redcristianauno.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.david.redcristianauno.domain.HistoricalWeeklyUseCase

class HistoryWeeklyViewModelFactory(private val historicalWeeklyUseCase: HistoricalWeeklyUseCase): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(HistoricalWeeklyUseCase::class.java).newInstance(historicalWeeklyUseCase)
    }
}
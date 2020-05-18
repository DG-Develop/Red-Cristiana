package com.david.redcristianauno.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.david.redcristianauno.domain.NoticeUseCase

class NoticeViewModelFactory(private val news: NoticeUseCase): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(NoticeUseCase::class.java).newInstance(news)
    }
}
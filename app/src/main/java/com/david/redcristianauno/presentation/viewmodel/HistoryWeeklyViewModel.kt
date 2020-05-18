package com.david.redcristianauno.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.david.redcristianauno.domain.HistoricalWeeklyUseCase
import com.david.redcristianauno.data.model.HistoricalWeekly
import com.david.redcristianauno.data.network.Callback
import java.lang.Exception

class HistoryWeeklyViewModel(historicalWeeklyUseCase: HistoricalWeeklyUseCase): ViewModel()  {
    private val historicalWeekly = historicalWeeklyUseCase
    var listHistorical: MutableLiveData<List<HistoricalWeekly>> = MutableLiveData()
    var isLoading = MutableLiveData<Boolean>()

    fun refresh(){
        getHistoricalWeeklyFromFirebase()
    }

    private fun getHistoricalWeeklyFromFirebase(){
        historicalWeekly.getAllHistoricalWeekly(object : Callback<List<HistoricalWeekly>> {
            override fun OnSucces(result: List<HistoricalWeekly>?) {
                listHistorical.postValue(result)
                processFinished()
            }

            override fun onFailure(exception: Exception) {

            }
        })
    }

    private fun processFinished() {
        isLoading.value = true
    }
}
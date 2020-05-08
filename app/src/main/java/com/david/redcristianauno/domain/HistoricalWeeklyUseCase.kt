package com.david.redcristianauno.domain

import com.david.redcristianauno.model.DataCelula
import com.david.redcristianauno.model.HistoricalWeekly
import com.david.redcristianauno.model.network.Callback

interface HistoricalWeeklyUseCase {
    fun getHistoricalWeeklyWithDate(dateSelected: String, callback: Callback<Boolean>)
    fun getDataCelula(callback: Callback<List<DataCelula>>)
}
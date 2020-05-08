package com.david.redcristianauno.model.network

import com.david.redcristianauno.model.DataCelula

interface HistoricalWeeklyRepository {
    fun getHistoricalWeeklyWithDate(date: String, callback: Callback<Boolean>)
    fun getDataCelula(callback: Callback<List<DataCelula>>)
}
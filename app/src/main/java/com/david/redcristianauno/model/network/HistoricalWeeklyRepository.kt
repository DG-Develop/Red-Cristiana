package com.david.redcristianauno.model.network

import com.david.redcristianauno.model.DataCelula
import com.david.redcristianauno.model.HistoricalWeekly
import com.david.redcristianauno.vo.Resource

interface HistoricalWeeklyRepository {
    fun getHistoricalWeeklyWithDate(date: String, callback: Callback<Boolean>)
    fun getDataCelula(callback: Callback<List<DataCelula>>)
    suspend fun getPermission(id_user: String): Resource<String>
    fun getAllHistoricalWeekly(callback: Callback<List<HistoricalWeekly>>)
}
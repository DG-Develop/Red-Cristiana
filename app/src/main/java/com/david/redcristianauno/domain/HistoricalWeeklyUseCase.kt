package com.david.redcristianauno.domain

import com.david.redcristianauno.data.model.DataCelula
import com.david.redcristianauno.data.model.HistoricalWeekly
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.vo.Resource

interface HistoricalWeeklyUseCase {
    fun getHistoricalWeeklyWithDate(dateSelected: String, callback: Callback<Boolean>)
    fun getDataCelula(callback: Callback<List<DataCelula>>)
    suspend fun getPermission(id_user: String): Resource<String>
    fun getAllHistoricalWeekly(callback: Callback<List<HistoricalWeekly>>)
}
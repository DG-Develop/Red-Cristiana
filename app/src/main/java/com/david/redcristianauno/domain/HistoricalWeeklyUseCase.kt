package com.david.redcristianauno.domain

import com.david.redcristianauno.model.DataCelula
import com.david.redcristianauno.model.HistoricalWeekly
import com.david.redcristianauno.model.network.Callback
import com.david.redcristianauno.vo.Resource

interface HistoricalWeeklyUseCase {
    fun getHistoricalWeeklyWithDate(dateSelected: String, callback: Callback<Boolean>)
    fun getDataCelula(callback: Callback<List<DataCelula>>)
    suspend fun getPermission(id_user: String): Resource<String>
}
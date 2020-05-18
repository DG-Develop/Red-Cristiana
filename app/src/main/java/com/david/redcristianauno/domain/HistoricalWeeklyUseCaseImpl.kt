package com.david.redcristianauno.domain

import com.david.redcristianauno.data.model.DataCelula
import com.david.redcristianauno.data.model.HistoricalWeekly
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.data.network.HistoricalWeeklyRepository
import com.david.redcristianauno.vo.Resource

class HistoricalWeeklyUseCaseImpl(private val historicalDaily: HistoricalWeeklyRepository) : HistoricalWeeklyUseCase {
    override fun getHistoricalWeeklyWithDate(dateSelected: String, callback: Callback<Boolean>) = historicalDaily.getHistoricalWeeklyWithDate(dateSelected, callback)
    override fun getDataCelula(callback: Callback<List<DataCelula>>) = historicalDaily.getDataCelula(callback)
    override suspend fun getPermission(id_user: String): Resource<String> = historicalDaily.getPermission(id_user)
    override fun getAllHistoricalWeekly(callback: Callback<List<HistoricalWeekly>>) = historicalDaily.getAllHistoricalWeekly(callback)
}
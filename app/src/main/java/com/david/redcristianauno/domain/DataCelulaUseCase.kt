package com.david.redcristianauno.domain

import com.david.redcristianauno.data.model.DataCelula
import com.david.redcristianauno.data.network.Callback

interface DataCelulaUseCase {
    fun getDataCelulaWithDate(dateSelected: String, callback: Callback<List<DataCelula>>)
}
package com.david.redcristianauno.domain

import com.david.redcristianauno.model.DataCelula
import com.david.redcristianauno.model.network.Callback

interface DataCelulaUseCase {
    fun getDataCelulaWithDate(dateSelected: String, callback: Callback<List<DataCelula>>)
}
package com.david.redcristianauno.domain

import com.david.redcristianauno.model.DataCelula
import com.david.redcristianauno.model.network.Callback
import com.david.redcristianauno.vo.Resource

interface DataCelulaUseCase {
    fun getDataCelulaWithDate(dateSelected: String, callback: Callback<List<DataCelula>>)
}
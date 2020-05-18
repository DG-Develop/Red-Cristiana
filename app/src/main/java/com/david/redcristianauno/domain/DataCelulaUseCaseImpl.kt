package com.david.redcristianauno.domain

import com.david.redcristianauno.data.model.DataCelula
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.data.network.DataCelulaRepository


class DataCelulaUseCaseImpl(private val dataCelula: DataCelulaRepository): DataCelulaUseCase {
    override fun getDataCelulaWithDate(dateSelected: String, callback: Callback<List<DataCelula>>) = dataCelula.getDataCelulaWithDate(dateSelected, callback)
}
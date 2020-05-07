package com.david.redcristianauno.domain

import com.david.redcristianauno.model.DataCelula
import com.david.redcristianauno.model.network.Callback
import com.david.redcristianauno.model.network.DataCelulaRepository


class DataCelulaUseCaseImpl(private val dataCelula: DataCelulaRepository): DataCelulaUseCase {
    override fun getDataCelulaWithDate(dateSelected: String, callback: Callback<List<DataCelula>>) = dataCelula.getDataCelulaWithDate(dateSelected, callback)
}
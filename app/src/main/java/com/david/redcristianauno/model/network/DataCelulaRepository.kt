package com.david.redcristianauno.model.network

import com.david.redcristianauno.model.DataCelula


interface DataCelulaRepository{
     fun getDataCelulaWithDate(dateSelected: String, callback: Callback<List<DataCelula>>)
}
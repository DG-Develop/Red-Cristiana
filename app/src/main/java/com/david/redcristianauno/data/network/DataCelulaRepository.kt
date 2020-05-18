package com.david.redcristianauno.data.network

import com.david.redcristianauno.data.model.DataCelula


interface DataCelulaRepository{
     fun getDataCelulaWithDate(dateSelected: String, callback: Callback<List<DataCelula>>)
}
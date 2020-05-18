package com.david.redcristianauno.presentation.viewmodel

import androidx.lifecycle.*
import com.david.redcristianauno.domain.DataCelulaUseCase
import com.david.redcristianauno.data.model.DataCelula
import com.david.redcristianauno.data.network.Callback
import java.lang.Exception


class DataCelulaViewModel(dataCelulaUseCase: DataCelulaUseCase): ViewModel() {
    val dataCelula = dataCelulaUseCase
    var listSchedule: MutableLiveData<List<DataCelula>> = MutableLiveData()
    var isLoading = MutableLiveData<Boolean>()

    fun refresh(dateSelected: String){
        getDataCelulaFromFirebase(dateSelected)
    }

    private fun getDataCelulaFromFirebase(dateSelected: String){
        dataCelula.getDataCelulaWithDate(dateSelected, object: Callback<List<DataCelula>>{
            override fun OnSucces(result: List<DataCelula>?) {
                listSchedule.postValue(result)
                processFinished()
            }

            override fun onFailure(exception: Exception) {
                processFinished()
            }
        })
    }

    private fun processFinished() {
        isLoading.value = true
    }

}
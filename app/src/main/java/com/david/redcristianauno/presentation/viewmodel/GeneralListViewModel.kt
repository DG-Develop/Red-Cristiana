package com.david.redcristianauno.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.david.redcristianauno.data.model.GeneralModel
import com.david.redcristianauno.data.model.Red
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.domain.ChurchUseCase
import java.lang.Exception

class GeneralListViewModel(churchUseCase: ChurchUseCase): ViewModel() {
    private val church = churchUseCase
    var redes = MutableLiveData<MutableList<GeneralModel>>()
    private val list: MutableList<GeneralModel> = mutableListOf()


    fun listRedesFromFirebase(name: String){
        church.getRedObject(name, object : Callback<MutableList<Red>>{
            override fun OnSucces(result: MutableList<Red>?) {
                if (result != null) {
                    for (red in result) {
                        list.add(GeneralModel(red.id_red, red.name_leader))
                    }
                }
                redes.postValue(list)
            }

            override fun onFailure(exception: Exception) {
                TODO("Not yet implemented")
            }
        })
    }
}
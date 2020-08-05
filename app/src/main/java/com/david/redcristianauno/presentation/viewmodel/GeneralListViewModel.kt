package com.david.redcristianauno.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.david.redcristianauno.data.model.GeneralModel
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.domain.ChurchUseCase
import java.lang.Exception

class GeneralListViewModel(churchUseCase: ChurchUseCase): ViewModel() {
    private val church = churchUseCase
    var redes = MutableLiveData<MutableList<GeneralModel>>()


    fun listRedesFromFirebase(name: String){
        church.getRedObject(name, object : Callback<MutableList<GeneralModel>>{
            override fun OnSucces(result: MutableList<GeneralModel>?) {
                redes.postValue(result)
            }

            override fun onFailure(exception: Exception) {
                TODO("Not yet implemented")
            }
        })
    }
}
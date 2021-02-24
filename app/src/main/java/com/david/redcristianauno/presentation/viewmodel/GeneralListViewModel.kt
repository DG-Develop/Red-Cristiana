package com.david.redcristianauno.presentation.viewmodel

import androidx.lifecycle.*
import com.david.redcristianauno.data.model.Celula
import com.david.redcristianauno.data.model.GeneralModel
import com.david.redcristianauno.data.model.Red
import com.david.redcristianauno.data.model.Subred
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.domain.ChurchUseCase
import java.lang.Exception

class GeneralListViewModel (churchUseCase: ChurchUseCase): ViewModel() {

    private val church = churchUseCase
    var redes = MutableLiveData<MutableList<GeneralModel>>()
    var subredes = MutableLiveData<MutableList<GeneralModel>>()
    var celulas = MutableLiveData<MutableList<GeneralModel>>()
    private val list: MutableList<GeneralModel> = mutableListOf()

    // Section fill dropdown list
    var dropdownRed = MutableLiveData<MutableList<String>>()
    var dropdownSubred = MutableLiveData<MutableList<String>>()

    // Functions
    fun listRedesFromFirebase(name: String){
        church.getRedObject(name, object : Callback<MutableList<Red>>{
            override fun OnSucces(result: MutableList<Red>?) {
                if (result != null) {
                    for (red in result) {
                        //list.add(GeneralModel(red.id_red, red.name_leader))
                    }
                }
                redes.postValue(list)
            }

            override fun onFailure(exception: Exception) {
                TODO("Not yet implemented")
            }
        })
    }

    fun listSubredesFromFirebase(iglesia: String, red: String){
        church.getSubredObject(iglesia, red, object : Callback<List<Subred>>{
            override fun OnSucces(result: List<Subred>?) {
                list.clear()
                if (result != null) {
                    for (subred in result) {
                        //list.add(GeneralModel(subred.id_subred, subred.name_leader))
                    }
                }
                subredes.postValue(list)
            }

            override fun onFailure(exception: Exception) {
                TODO("Not yet implemented")
            }
        })
    }

    fun listCelulasFromFirebase(iglesia: String, red: String, subred: String){
        if (subred.isBlank()){
            list.clear()
            return celulas.postValue(list)
        }
        church.getCelulaObject(iglesia, red, subred, object : Callback<MutableList<Celula>>{
            override fun OnSucces(result: MutableList<Celula>?) {
                list.clear()
                if (result != null) {
                    for (celula in result) {
                        //list.add(GeneralModel(celula.id_celula, celula.name_leader))
                    }
                }
                celulas.postValue(list)
            }

            override fun onFailure(exception: Exception) {
                TODO("Not yet implemented")
            }
        })
    }

    // functions fill dropdown
    fun fillTilRed(nameChurch: String){
        church.getRedes(nameChurch, object : Callback<MutableList<String>>{
            override fun OnSucces(result: MutableList<String>?) {
                dropdownRed.postValue(result)
            }

            override fun onFailure(exception: Exception) {
                TODO("Not yet implemented")
            }
        })
    }

    fun fillTilSubred(iglesia: String, red: String){
        church.getSubredes(iglesia, red, object : Callback<MutableList<String>>{
            override fun OnSucces(result: MutableList<String>?) {
                dropdownSubred.postValue(result)
            }

            override fun onFailure(exception: Exception) {

            }
        })
    }
}
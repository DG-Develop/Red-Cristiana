package com.david.redcristianauno.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.david.redcristianauno.data.model.Iglesia
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.domain.ChurchUseCase
import com.google.firebase.firestore.DocumentReference
import java.lang.Exception


class JoinViewModel (churchUseCase: ChurchUseCase): ViewModel(){
    private val church = churchUseCase
    var iglesias = MutableLiveData<MutableList<Iglesia>>()
    var redes = MutableLiveData<MutableList<String>>()
    var subredes = MutableLiveData<MutableList<String>>()
    var celulas = MutableLiveData<MutableList<String>>()

    fun fillTilIglesia(){
        church.getIglesias(object : Callback<MutableList<Iglesia>>{
            override fun OnSucces(result: MutableList<Iglesia>?) {
                iglesias.postValue(result)
            }

            override fun onFailure(exception: Exception) {

            }
        })
    }

    fun fillTilRed(name: String){
        church.getRedes(name, object : Callback<MutableList<String>>{
            override fun OnSucces(result: MutableList<String>?) {
                redes.postValue(result)
            }

            override fun onFailure(exception: Exception) {
                TODO("Not yet implemented")
            }
        })
    }

    fun fillTilSubred(iglesia: String, red: String){
        church.getSubredes(iglesia, red, object : Callback<MutableList<String>>{
            override fun OnSucces(result: MutableList<String>?) {
                subredes.postValue(result)
            }

            override fun onFailure(exception: Exception) {

            }
        })
    }

    fun fillTilCelula(iglesia: String, red: String, subred: String){
        church.getCelulas(iglesia, red, subred, object : Callback<MutableList<String>>{
            override fun OnSucces(result: MutableList<String>?) {
                celulas.postValue(result)
            }

            override fun onFailure(exception: Exception) {

            }
        })
    }

    fun updateDataChurchFromFirebase(iglesia_references: DocumentReference){
        church.updateDataChurch(iglesia_references)
    }

}
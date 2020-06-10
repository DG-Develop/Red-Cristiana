package com.david.redcristianauno.presentation.viewmodel

import android.content.Context
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.domain.UserUseCase
import com.david.redcristianauno.vo.Resource
import kotlinx.android.synthetic.main.fragment_data_celula_dialog.*
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class UserViewModel(userUseCase: UserUseCase): ViewModel() {
    lateinit var id_user:String
    private val type = userUseCase
    var subredes = MutableLiveData<MutableList<String>>()
    var celulas = MutableLiveData<MutableList<String>>()

    val fetchNameUser = liveData(Dispatchers.IO) {
        emit(Resource.Loading())

        try {
            val userName = userUseCase.getNamesUsers(id_user)
            emit(userName)
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }

    fun fillTilCelula(subred: String){
        Log.i("DataInfo", "Celula:$subred ")
        type.getCelulasForFillTil(subred, object: Callback<MutableList<String>>{
            override fun OnSucces(result: MutableList<String>?) {
                celulas.postValue(result)
            }

            override fun onFailure(exception: Exception) {

            }
        })
    }

    fun fillTilSubred(){
        type.getSubredesForFillTil(object : Callback<MutableList<String>>{
            override fun OnSucces(result: MutableList<String>?) {
                subredes.postValue(result)
            }

            override fun onFailure(exception: Exception) {

            }
        })
    }
}
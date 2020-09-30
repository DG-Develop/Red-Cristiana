package com.david.redcristianauno.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.david.redcristianauno.data.model.CreateEntityModel
import com.david.redcristianauno.data.model.Subred
import com.david.redcristianauno.data.model.User
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.domain.ChurchUseCase
import java.lang.Exception

class CreateEntityViewModel(churchUseCase: ChurchUseCase) : ViewModel(){
    private val church = churchUseCase
    var redesList = MutableLiveData<MutableList<String>>() //Para rellenar el comboBox

    var users = MutableLiveData<ArrayList<User>>()
    var subred = MutableLiveData<ArrayList<CreateEntityModel>>()

    val listUser: ArrayList<User> = ArrayList()
    val listEntity: ArrayList<CreateEntityModel> = ArrayList()

    val listFoundUsers: ArrayList<User> = ArrayList()
    val listFoundEntity: ArrayList<CreateEntityModel> = ArrayList()

    fun search(char: String, key: String){
        searchUserWithoutSomeParamsFromFirebase(char, key)
    }

    fun listUsersFromFirebase(){
        church.getListUser(object : Callback<List<User>>{
            override fun OnSucces(result: List<User>?) {
                if (result != null) {
                    for (user in result){
                        listUser.add(user)
                    }
                }
                users.postValue(listUser)
            }

            override fun onFailure(exception: Exception) {
                TODO("Not yet implemented")
            }
        })
    }

    fun listSubredFromFirebase(iglesia: String, red: String){
        church.getSubredObject(iglesia, red, object : Callback<List<Subred>>{
            override fun OnSucces(result: List<Subred>?) {
                if (result != null) {
                    for(subred in result){
                        listEntity.add(CreateEntityModel(subred.id_subred, "", "Subred"))
                    }
                }
                subred.postValue(listEntity)
            }

            override fun onFailure(exception: Exception) {
                TODO("Not yet implemented")
            }
        })
    }

    fun fillTilRedFromFirebase(churchName: String){
        church.getRedes(churchName, object : Callback<MutableList<String>>{
            override fun OnSucces(result: MutableList<String>?) {
                redesList.postValue(result)
            }

            override fun onFailure(exception: Exception) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun searchUserWithoutSomeParamsFromFirebase(char: String, key: String){
        church.searchUserWithoutSomeParams(char, key, object: Callback<List<User>>{
            override fun OnSucces(result: List<User>?) {
                listFoundUsers.clear()
                if (result != null) {
                    for(user in result){
                        listFoundUsers.add(user)
                    }
                }
                users.postValue(listFoundUsers)
            }

            override fun onFailure(exception: Exception) {
                TODO("Not yet implemented")
            }
        })
    }

    fun refreshListFromFirebase(iglesia: String, red: String){
        church.getSubredObject(iglesia, red, object : Callback<List<Subred>>{
            override fun OnSucces(result: List<Subred>?) {
                listFoundEntity.clear()
                if (result != null) {
                    for(subred in result){
                        listFoundEntity.add(CreateEntityModel(subred.id_subred, "", "Subred"))
                    }
                }
                subred.postValue(listFoundEntity)
            }

            override fun onFailure(exception: Exception) {
                TODO("Not yet implemented")
            }
        })
    }

}
package com.david.redcristianauno.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.david.redcristianauno.domain.ConfigurationUseCase
import com.david.redcristianauno.data.model.User
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.presentation.objectsUtils.UserSingleton
import java.lang.Exception

class ConfigurationViewModel(configUseCase: ConfigurationUseCase): ViewModel(){
    private val userLogin = UserSingleton.getUser()
    private val users = configUseCase
    var listUser: MutableLiveData<List<User>> = MutableLiveData()
    var isLoading = MutableLiveData<Boolean>()

    fun refresh(type: String){
        getUsersFromFirebase(type)
    }

    fun refreshPostulates(type: String){
        getUsersPostulatesFromFirebase(type)
    }

    fun search(name: String){
        searchUserFromFirebase(name)
    }

    private fun getUsersFromFirebase(type: String){
        users.getTypeUsers(type, object : Callback<List<User>>{
            override fun OnSucces(result: List<User>?) {
                listUser.postValue(result)
                processFinished()
                if (result.isNullOrEmpty()){
                    processFinished()
                }
            }

            override fun onFailure(exception: Exception) {

            }
        })
    }

    private fun getUsersPostulatesFromFirebase(type: String){
        userLogin?.iglesia_references?.let {
            users.getPostulateUsers(type, it, object : Callback<List<User>>{
                override fun OnSucces(result: List<User>?) {
                    listUser.postValue(result)
                    processFinished()
                    if (result.isNullOrEmpty()){
                        processFinished()
                    }
                }

                override fun onFailure(exception: Exception) {

                }
            })
        }
    }

    fun updateUserFromFirebase(id: String, permissionType: String){
        users.updateUser(id, permissionType)
    }

    fun deleteUserFromFirebase(id: String){
        users.deleteUser(id)
    }

    private fun searchUserFromFirebase(name: String){
        users.searchUser(name, object: Callback<List<User>>{
            override fun OnSucces(result: List<User>?) {
                listUser.postValue(result)
                processFinished()
            }

            override fun onFailure(exception: Exception) {

            }
        })
    }

    private fun processFinished() {
        isLoading.value = true
    }

}
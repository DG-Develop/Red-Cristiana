package com.david.redcristianauno.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.david.redcristianauno.data.model.CreateEntityModel
import com.david.redcristianauno.data.model.User
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.domain.ChurchUseCase
import java.lang.Exception

class CreateEntityViewModel(churchUseCase: ChurchUseCase) : ViewModel(){
    private val church = churchUseCase

    var users: MutableLiveData<List<User>> = MutableLiveData()
    var subred = MutableLiveData<ArrayList<CreateEntityModel>>()

    val listFoundUsers: ArrayList<User> = ArrayList()

    fun search(char: String, key: String){
        searchUserWithoutSomeParamsFromFirebase(char, key)
    }

    fun listUsersFromFirebase(){
        church.getListUser(object : Callback<List<User>>{
            override fun OnSucces(result: List<User>?) {
                users.postValue(result)
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


}
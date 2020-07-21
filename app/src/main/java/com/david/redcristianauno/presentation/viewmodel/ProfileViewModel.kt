package com.david.redcristianauno.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.david.redcristianauno.domain.ProfileUseCase
import com.david.redcristianauno.data.model.User
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.Dispatchers.IO
import java.lang.Exception

class ProfileViewModel(profile: ProfileUseCase) : ViewModel() {
    private val user = profile
    var userData: MutableLiveData<User> = MutableLiveData()
    var isLoading = MutableLiveData<Boolean>()

    fun refresh() {
        getDataUserFromFirebase()
    }

    private fun getDataUserFromFirebase() {
        user.getDataUser(object : Callback<User> {
            override fun OnSucces(result: User?) {
                userData.postValue(result)
                processFinished()
            }

            override fun onFailure(exception: Exception) {

            }
        })
    }

    fun getDataUserAsyncFromFirebase(): LiveData<Resource<User>> {
        return liveData(IO){
            emit(Resource.Loading())
            try {
                val userFound = user.getDataUserAsync()
                emit(userFound)
            }catch (e: Exception){
                emit(Resource.Failure(e))
            }
        }
    }

    fun updateDataUserFromFirebase(
        names: String, last_names: String, telephone: String, address: String
    ) {
        user.updateDataUser(names, last_names, telephone, address)
    }

    private fun processFinished() {
        isLoading.value = true
    }
}
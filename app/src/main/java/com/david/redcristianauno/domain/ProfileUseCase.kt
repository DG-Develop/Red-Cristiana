package com.david.redcristianauno.domain

import com.david.redcristianauno.data.model.User
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.vo.Resource

interface ProfileUseCase {
    fun getDataUser(callback: Callback<User>)
    fun updateDataUser(names: String, last_names: String, telephone: String, address: String)
    suspend fun getDataUserAsync(): Resource<User>
}
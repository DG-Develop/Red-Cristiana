package com.david.redcristianauno.domain

import com.david.redcristianauno.model.User
import com.david.redcristianauno.model.network.Callback

interface ConfigurationUseCase {
    fun getTypeUsers(type: String, callback: Callback<List<User>>)
    fun updateUser(id: String, permissionType: String)
    fun deleteUser(id: String)
    fun searchUser(name: String, callback: Callback<List<User>>)
}
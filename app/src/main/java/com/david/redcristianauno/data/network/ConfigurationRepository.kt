package com.david.redcristianauno.data.network

import com.david.redcristianauno.data.model.User

interface ConfigurationRepository {
    fun getTypeUsers(type: String, callback: Callback<List<User>>)
    fun updateUserFromPermission(id: String, permissionType: String)
    fun deleteUser(id: String)
    fun searchUser(name: String, callback: Callback<List<User>>)
}
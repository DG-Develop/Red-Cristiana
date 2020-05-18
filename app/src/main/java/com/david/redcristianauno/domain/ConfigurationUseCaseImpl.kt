package com.david.redcristianauno.domain

import com.david.redcristianauno.data.model.User
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.data.network.ConfigurationRepository

class ConfigurationUseCaseImpl(private val config: ConfigurationRepository) : ConfigurationUseCase {
    override fun getTypeUsers(type: String, callback: Callback<List<User>>) = config.getTypeUsers(type, callback)
    override fun updateUser(id: String, permissionType: String)  = config.updateUserFromPermission(id, permissionType)
    override fun deleteUser(id: String) = config.deleteUser(id)
    override fun searchUser(name: String, callback: Callback<List<User>>) = config.searchUser(name, callback)
}
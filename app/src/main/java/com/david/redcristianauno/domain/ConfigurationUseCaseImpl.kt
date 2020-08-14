package com.david.redcristianauno.domain

import com.david.redcristianauno.data.model.User
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.data.network.ConfigurationRepository
import com.google.firebase.firestore.DocumentReference

class ConfigurationUseCaseImpl(private val config: ConfigurationRepository) : ConfigurationUseCase {
    override fun getTypeUsers(type: String, callback: Callback<List<User>>)
            = config.getTypeUsers(type, callback)

    override fun getPostulateUsers(
        type: String, iglesia_references: DocumentReference, callback: Callback<List<User>>
    ) = config.getPostulateUsers(type, iglesia_references, callback)

    override fun updateUser(id: String, permissionType: String)
            = config.updateUserFromPermission(id, permissionType)
    override fun deleteUser(id: String) = config.deleteUser(id)
    override fun searchUser(char: String, key: String, callback: Callback<List<User>>)
            = config.searchUser(char, key, callback)
}
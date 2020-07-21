package com.david.redcristianauno.data.network

import com.david.redcristianauno.data.model.User
import com.google.firebase.firestore.DocumentReference

interface ConfigurationRepository {
    fun getTypeUsers(type: String, callback: Callback<List<User>>)
    fun getPostulateUsers(
        type: String, iglesia_references: DocumentReference, callback: Callback<List<User>>
    )
    fun updateUserFromPermission(id: String, permissionType: String)
    fun deleteUser(id: String)
    fun searchUser(name: String, callback: Callback<List<User>>)
}
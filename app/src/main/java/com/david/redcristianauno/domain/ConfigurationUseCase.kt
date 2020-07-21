package com.david.redcristianauno.domain

import com.david.redcristianauno.data.model.User
import com.david.redcristianauno.data.network.Callback
import com.google.firebase.firestore.DocumentReference

interface ConfigurationUseCase {
    fun getTypeUsers(type: String, callback: Callback<List<User>>)
    fun getPostulateUsers(
        type: String, iglesia_references: DocumentReference, callback: Callback<List<User>>
    )
    fun updateUser(id: String, permissionType: String)
    fun deleteUser(id: String)
    fun searchUser(name: String, callback: Callback<List<User>>)
}
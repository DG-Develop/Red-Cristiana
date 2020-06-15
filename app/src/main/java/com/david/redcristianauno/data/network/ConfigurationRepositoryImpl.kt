package com.david.redcristianauno.data.network

import android.util.Log
import com.david.redcristianauno.data.model.User
import com.david.redcristianauno.data.network.FirebaseService.Companion.USER_COLLECTION_NAME
import com.google.firebase.auth.AuthCredential

class ConfigurationRepositoryImpl : ConfigurationRepository {
    private val firebaseService = FirebaseService()

    override fun getTypeUsers(type: String, callback: Callback<List<User>>) {
        firebaseService.firebaseFirestore.collection(USER_COLLECTION_NAME)
            .whereEqualTo("permission", type)
            //.orderBy("names")
            .get()
            .addOnSuccessListener { result ->
                lateinit var list: List<User>
                if (!result.isEmpty) {
                    for (doc in result) {
                        list = result.toObjects(User::class.java)
                        callback.OnSucces(list)
                        break
                    }
                }else{
                    list = result.toObjects(User::class.java)
                    callback.OnSucces(list)
                }
            }
    }

    override fun updateUserFromPermission(id: String, permissionType: String) {
        firebaseService.firebaseFirestore.collection(USER_COLLECTION_NAME)
            .document(id)
            .update("permission", permissionType)
            .addOnSuccessListener { Log.i("UserInfo", "Update Success") }
            .addOnFailureListener{e -> Log.i("UserInfo", "Error updating document", e)}
    }

    override fun deleteUser(id: String) {
        firebaseService.firebaseFirestore.collection(USER_COLLECTION_NAME)
            .document(id)
            .delete()
            .addOnSuccessListener { Log.i("UserInfo", "Delete Success") }
            .addOnFailureListener{e -> Log.i("UserInfo", "Error deleting document", e)}
    }

    override fun searchUser(name: String, callback: Callback<List<User>>) {
        firebaseService.firebaseFirestore.collection(USER_COLLECTION_NAME)
            .orderBy("names")
            .startAt(name).endAt(name + "\uf8ff") //"\uf8ff"
            .get()
            .addOnSuccessListener { result ->
                lateinit var list: List<User>
                if (!result.isEmpty) {
                    for (doc in result) {
                        list = result.toObjects(User::class.java)
                        callback.OnSucces(list)
                        break
                    }
                }else{
                    list = result.toObjects(User::class.java)
                    callback.OnSucces(list)
                }
            }
    }
}
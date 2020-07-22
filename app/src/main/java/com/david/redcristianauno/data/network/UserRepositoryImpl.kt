package com.david.redcristianauno.data.network


import android.util.Log
import com.david.redcristianauno.data.model.Subred
import com.david.redcristianauno.data.model.User
import com.david.redcristianauno.data.network.FirebaseService.Companion.USER_COLLECTION_NAME
import com.david.redcristianauno.presentation.objectsUtils.UserSingleton
import com.david.redcristianauno.presentation.ui.activities.JoinActivity
import com.david.redcristianauno.vo.Resource
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl : UserRepository {
    val firebaseService = FirebaseService()

    override suspend fun getNamesUsers(id_user: String): Resource<String> {
        val resultData = firebaseService.firebaseFirestore
            .collection(USER_COLLECTION_NAME)
            .document(id_user)
            .get()
            .await()

        val nameUser = resultData.getString("names")
        return Resource.Success(nameUser!!)
    }


    override fun getSubredesForFillTil(callback: Callback<MutableList<String>>) {
        firebaseService.firebaseFirestore.collection(FirebaseService.REDES_COLLECTION_NAME)
            .document("Vida de Jesús")
            .collection(FirebaseService.SUBREDES_COLLECTION_NAME)
            .get()
            .addOnSuccessListener { result ->
                val spinnerSubred: MutableList<String> = mutableListOf()
                val list = result.toObjects(Subred::class.java)
                for (name in list) {
                    spinnerSubred.add(name.id_subred)
                }
                callback.OnSucces(spinnerSubred)
            }
    }

    override fun getCelulasForFillTil(subred: String, callback: Callback<MutableList<String>>) {
        firebaseService.firebaseFirestore.collection(FirebaseService.REDES_COLLECTION_NAME)
            .document("Vida de Jesús")
            .collection(FirebaseService.SUBREDES_COLLECTION_NAME)
            .document(subred)
            .get()
            .addOnSuccessListener { result ->
                val spinnerCelula: MutableList<String> = mutableListOf()
                val celula = result.toObject(Subred::class.java)
                val list = celula?.celulas
                if (list != null) {
                    for (name in list) {
                        spinnerCelula.add(name)
                    }
                }
                callback.OnSucces(spinnerCelula)
            }
    }

    override fun getDataUser(callback: Callback<User>) {
        firebaseService.firebaseFirestore.collection(USER_COLLECTION_NAME)
            .document(firebaseService.firebaseAuth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener { result ->
                val foundUser = result.toObject(User::class.java)
                callback.OnSucces(foundUser)
            }
    }

    override suspend fun getDataUserAsync(): Resource<User> {
        val userFound = firebaseService.firebaseFirestore.collection(USER_COLLECTION_NAME)
            .document(firebaseService.firebaseAuth.currentUser?.uid.toString())
            .get()
            .await()

        val user = userFound.toObject(User::class.java)
        return Resource.Success(user!!)
    }

    override fun updateDataUser(
        names: String, last_names: String, telephone: String, address: String
    ) {
        firebaseService.firebaseFirestore.collection(USER_COLLECTION_NAME)
            .document(firebaseService.firebaseAuth.currentUser?.uid.toString())
            .update(
                mapOf(
                    "names" to names,
                    "last_names" to last_names,
                    "telephone" to telephone,
                    "address" to address
                )
            )
            .addOnSuccessListener { Log.i("UserInfo", "Update Success") }
            .addOnFailureListener { e -> Log.i("UserInfo", "Error updating document", e) }
    }

    override fun updateDataChurch(iglesia_references: DocumentReference) {
        firebaseService.firebaseFirestore.collection(USER_COLLECTION_NAME)
            .document(firebaseService.firebaseAuth.currentUser?.uid.toString())
            .update(mapOf("iglesia_references" to iglesia_references))
            .addOnSuccessListener {
                Log.i(JoinActivity.TAG, "Update Success")
                UserSingleton.updateChurch(iglesia_references)
            }
            .addOnFailureListener { e -> Log.i(JoinActivity.TAG, "Error updating document", e) }
    }
}
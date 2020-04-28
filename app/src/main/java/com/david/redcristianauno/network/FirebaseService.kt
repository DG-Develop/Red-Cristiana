package com.david.redcristianauno.network

import com.david.redcristianauno.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

const val USERS = "users"

class FirebaseService {
    val firebaseFirestore = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    private var dbReference : DatabaseReference

    val settings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build()

    init {
        firebaseFirestore.firestoreSettings = settings
        dbReference = database.reference
    }

    fun checkNickname(nickname: String, callback: Callback<User>){
        firebaseFirestore.collection(USERS).document(nickname)
            .get()
            .addOnSuccessListener { result->
                if(result.data != null){
                    callback.OnSucces(result.toObject(User::class.java))
                }else
                    callback.OnSucces(null)
            }.addOnFailureListener{ exception -> callback.onFailure(exception)}
    }

   fun setDocumentWithID(data: Any, collectionName: String, id: String, callback: Callback<Void>){
       firebaseFirestore.collection(collectionName).document(id).set(data)
           .addOnSuccessListener { callback.OnSucces(null) }
           .addOnFailureListener{ exception -> callback.onFailure(exception) }
    }

    fun setDocumentWithOutID(data: Any, collectionName: String, callback: Callback<Void>){
        firebaseFirestore.collection(collectionName).document().set(data)
            .addOnSuccessListener { callback.OnSucces(null) }
            .addOnFailureListener{ exception -> callback.onFailure(exception) }
    }




}
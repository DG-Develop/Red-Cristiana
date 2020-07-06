package com.david.redcristianauno.data.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirebaseService {
    val firebaseFirestore = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    private var dbReference : DatabaseReference
    var mStorage: StorageReference

    val settings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build()

    init {
        firebaseFirestore.firestoreSettings = settings
        dbReference = database.reference
        mStorage = FirebaseStorage.getInstance().reference
    }


   fun setDocumentWithID(data: Any, collectionName: String, id: String, callback: Callback<Void>){
       firebaseFirestore.collection(collectionName).document(id).set(data)
           .addOnSuccessListener { callback.OnSucces(null) }
           .addOnFailureListener{ exception -> callback.onFailure(exception) }
    }

    fun setDocumentWithID(data: Any, collectionName: String, id: String){
        firebaseFirestore.collection(collectionName).document(id).set(data)
    }

    fun setDocumentWithOutID(data: Any, collectionName: String, callback: Callback<Void>){
        firebaseFirestore.collection(collectionName).document().set(data)
            .addOnSuccessListener { callback.OnSucces(null) }
            .addOnFailureListener{ exception -> callback.onFailure(exception) }
    }

    companion object{
        const val IGLESIA_COLLECTION_NAME = "iglesia"
        const val USER_COLLECTION_NAME = "users"
        const val DATA_CELULA_COLLECTION_NAME = "data celula"
        const val HISTORICAL_WEEKLY_COLLECTION_NAME = "historical weekly"
        const val REDES_COLLECTION_NAME = "redes"
        const val SUBREDES_COLLECTION_NAME = "subredes"
        const val CELULA_COLLECTION_NAME = "celulas"
        const val NEWS_COLLECTION_NAME = "news"
    }
}
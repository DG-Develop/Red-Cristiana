package com.david.redcristianauno.data.network

import android.util.Log
import com.david.redcristianauno.data.model.Celula
import com.david.redcristianauno.data.model.Iglesia
import com.david.redcristianauno.data.model.Red
import com.david.redcristianauno.data.model.Subred

class ChurchRepositoryImpl : ChurchRepository {
    val firebaseService = FirebaseService()

    override fun getIglesias(callback: Callback<MutableList<Iglesia>>) {
        firebaseService.firebaseFirestore.collection(FirebaseService.IGLESIA_COLLECTION_NAME)
            .get()
            .addOnSuccessListener { result ->
                val list = result.toObjects(Iglesia::class.java)
                callback.OnSucces(list)
            }
    }

    override fun getRedes(name: String, callback: Callback<MutableList<String>>) {
        firebaseService.firebaseFirestore.collection(
            "${FirebaseService.IGLESIA_COLLECTION_NAME}/" +
                    "${name}/" +
                    FirebaseService.REDES_COLLECTION_NAME
        )
            .get()
            .addOnSuccessListener { result ->
                val dropdown: MutableList<String> = mutableListOf()
                val redesList = result.toObjects(Red::class.java)

                for (redName in redesList) {
                    Log.i(TAG, "name: ${redName.id_red}")
                    dropdown.add(redName.id_red)
                }

                callback.OnSucces(dropdown)
            }

    }

    override fun getSubredes(
        iglesia: String, red: String, callback: Callback<MutableList<String>>
    ) {
        firebaseService.firebaseFirestore.collection(
            "${FirebaseService.IGLESIA_COLLECTION_NAME}/" +
                    "${iglesia}/" +
                    "${FirebaseService.REDES_COLLECTION_NAME}/" +
                    "${red}/" +
                    FirebaseService.SUBREDES_COLLECTION_NAME
        )
            .get()
            .addOnSuccessListener { result ->
                val dropdown: MutableList<String> = mutableListOf()
                val subredesList = result.toObjects(Subred::class.java)

                for (subredName in subredesList) {
                    Log.i(TAG, "name: ${subredName.id_subred}")
                    dropdown.add(subredName.id_subred)
                }

                callback.OnSucces(dropdown)
            }
    }

    override fun getCelulas(
        iglesia: String, red: String, subred: String, callback: Callback<MutableList<String>>
    ) {
        firebaseService.firebaseFirestore.collection(
            "${FirebaseService.IGLESIA_COLLECTION_NAME}/" +
                    "${iglesia}/" +
                    "${FirebaseService.REDES_COLLECTION_NAME}/" +
                    "${red}/" +
                    "${FirebaseService.SUBREDES_COLLECTION_NAME}/" +
                    "${subred}/" +
                    FirebaseService.CELULA_COLLECTION_NAME
        )
            .get()
            .addOnSuccessListener { result ->
                val dropdown: MutableList<String> = mutableListOf()
                val celulaList = result.toObjects(Celula::class.java)

                for (cellulaNames in celulaList) {
                    Log.i(TAG, "name: ${cellulaNames.id_celula}")
                    dropdown.add(cellulaNames.id_celula)
                }

                callback.OnSucces(dropdown)
            }
    }

    companion object {
        private const val TAG = "JoinInfo"
    }
}
package com.david.redcristianauno.model.network

import com.david.redcristianauno.model.DataCelula
import com.david.redcristianauno.model.network.FirebaseService.Companion.DATA_CELULA_COLLECTION_NAME

class DataCelulaRepositoryImpl : DataCelulaRepository {
    private val firebaseService = FirebaseService()

    override fun getDataCelulaWithDate(dateSelected: String, callback: Callback<List<DataCelula>>) {
        firebaseService.firebaseFirestore.collection(DATA_CELULA_COLLECTION_NAME)
            .whereEqualTo("date", dateSelected)
            .get()
            .addOnSuccessListener { result ->
                lateinit var list: List<DataCelula>
                if (!result.isEmpty) {
                    for (doc in result) {
                        list = result.toObjects(DataCelula::class.java)
                        callback.OnSucces(list)
                        break
                    }
                }else{
                    list = result.toObjects(DataCelula::class.java)
                    callback.OnSucces(list)
                }
            }
    }
}
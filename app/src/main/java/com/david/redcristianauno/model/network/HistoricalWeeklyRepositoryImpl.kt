package com.david.redcristianauno.model.network

import android.util.Log
import com.david.redcristianauno.model.DataCelula
import com.david.redcristianauno.model.HistoricalWeekly
import com.david.redcristianauno.model.network.FirebaseService.Companion.HISTORICAL_WEEKLY_COLLECTION_NAME

class HistoricalWeeklyRepositoryImpl : HistoricalWeeklyRepository {
    private val firebaseService = FirebaseService()

    override fun getHistoricalWeeklyWithDate(date: String, callback: Callback<Boolean>) {
        firebaseService.firebaseFirestore.collection(HISTORICAL_WEEKLY_COLLECTION_NAME)
            .document(date)
            .get()
            .addOnSuccessListener { document ->
                val historicalWeekly = document.toObject(HistoricalWeekly::class.java)
                if(historicalWeekly != null){
                    callback.OnSucces(true)
                }else{
                    callback.OnSucces(false)
                }

            }.addOnFailureListener{
                Log.i("InfoMain", "Entro en el Failure")
                callback.OnSucces(false)
            }
    }

    override fun getDataCelula(callback: Callback<List<DataCelula>>) {
        firebaseService.firebaseFirestore.collection(FirebaseService.DATA_CELULA_COLLECTION_NAME)
            .get()
            .addOnSuccessListener { result ->
                for(doc in result){
                    val list = result.toObjects(DataCelula::class.java)
                    callback.OnSucces(list)
                    break
                }
            }
    }
}
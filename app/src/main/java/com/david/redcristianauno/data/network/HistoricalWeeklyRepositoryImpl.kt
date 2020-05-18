package com.david.redcristianauno.data.network

import android.util.Log
import com.david.redcristianauno.data.model.DataCelula
import com.david.redcristianauno.data.model.HistoricalWeekly
import com.david.redcristianauno.data.network.FirebaseService.Companion.HISTORICAL_WEEKLY_COLLECTION_NAME
import com.david.redcristianauno.data.network.FirebaseService.Companion.USER_COLLECTION_NAME
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.tasks.await

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

    override suspend fun getPermission(id_user: String): Resource<String> {
        val resultData = firebaseService.firebaseFirestore
            .collection(USER_COLLECTION_NAME)
            .document(id_user)
            .get()
            .await()

        val permissionUser = resultData.getString("permission")

        return Resource.Success(permissionUser!!)
    }

    override fun getAllHistoricalWeekly(callback: Callback<List<HistoricalWeekly>>) {
        firebaseService.firebaseFirestore.collection(HISTORICAL_WEEKLY_COLLECTION_NAME)
            .get()
            .addOnSuccessListener { result ->
                for (doc in result){
                    val list = result.toObjects(HistoricalWeekly::class.java)
                    callback.OnSucces(list)
                    break
                }
            }
    }
}
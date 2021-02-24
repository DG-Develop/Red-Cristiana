package com.david.redcristianauno.data.remote

import android.util.Log
import com.david.redcristianauno.application.AppConstants.CAPTURE_USER_FRAGMENT
import com.david.redcristianauno.application.AppConstants.CELL_COLLECTION_NAME
import com.david.redcristianauno.application.AppConstants.CHURCH_COLLECTION_NAME
import com.david.redcristianauno.application.AppConstants.NET_COLLECTION_NAME
import com.david.redcristianauno.application.AppConstants.SUBNET_COLLECTION_NAME
import com.david.redcristianauno.data.network.FirebaseService
import com.david.redcristianauno.domain.models.*
import com.david.redcristianauno.vo.Resource
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteChurchDataSourceImpl @Inject constructor(
    private val firebaseService: FirebaseService
) : RemoteChurchDataSource {

    override suspend fun getNetwork(church: String): Resource<List<NetWork>> {
        val result = firebaseService.firebaseFirestore.collection(
            "${CHURCH_COLLECTION_NAME}/" +
                    "${church}/" +
                    NET_COLLECTION_NAME
        )
            .get()
            .await()

        val listNet = result.toObjects(NetworkDataSource::class.java)

        val listNetResult = listNet.map { networkDataSource ->
            val searchLeader = firebaseService.firebaseFirestore
                .document(networkDataSource.leader!!.path)
                .get().await()
            val searchCreate = firebaseService.firebaseFirestore
                .document(networkDataSource.created_by!!.path)
                .get().await()

            NetWork(
                networkDataSource.id_red,
                searchCreate.toObject(UserDataSource::class.java)!!,
                searchLeader.toObject(UserDataSource::class.java)!!
            )
        }
        return Resource.Success(listNetResult)
    }

    override suspend fun getSubNetwork(
        church: String,
        network: String
    ): Resource<List<SubNetwork>> {
        val result = firebaseService.firebaseFirestore.collection(
            "${CHURCH_COLLECTION_NAME}/" +
                    "${church}/" +
                    "${NET_COLLECTION_NAME}/" +
                    "${network}/" +
                    SUBNET_COLLECTION_NAME
        )
            .get()
            .await()

        val listSubNet = result.toObjects(SubNetworkDataSource::class.java)

        val listSubNetResult = listSubNet.map { subNetworkDataSource ->
            val searchLeader = firebaseService.firebaseFirestore
                .document(subNetworkDataSource.leader!!.path)
                .get().await()
            val searchCreate = firebaseService.firebaseFirestore
                .document(subNetworkDataSource.created_by!!.path)
                .get().await()

            SubNetwork(
                subNetworkDataSource.id_subred,
                searchCreate.toObject(UserDataSource::class.java)!!,
                searchLeader.toObject(UserDataSource::class.java)!!
            )
        }
        return Resource.Success(listSubNetResult)
    }

    override suspend fun getCell(
        church: String,
        network: String,
        subNetwork: String
    ): Resource<List<CellDataSource>> {
        val result = firebaseService.firebaseFirestore
            .collection(
                "${CHURCH_COLLECTION_NAME}/" +
                        "${church}/" +
                        "${NET_COLLECTION_NAME}/" +
                        "${network}/" +
                        "${SUBNET_COLLECTION_NAME}/" +
                        "${subNetwork}/" +
                        CELL_COLLECTION_NAME
            )
            .get()
            .await()

        val listCell = result.toObjects(CellDataSource::class.java)
        return Resource.Success(listCell)
    }

    override suspend fun getPathCell(
        church: String,
        network: String,
        subNetwork: String,
        cell: String
    ): DocumentReference {
        val result = firebaseService.firebaseFirestore
            .collection(
                "${CHURCH_COLLECTION_NAME}/" +
                        "${church}/" +
                        "${NET_COLLECTION_NAME}/" +
                        "${network}/" +
                        "${SUBNET_COLLECTION_NAME}/" +
                        "${subNetwork}/" +
                        CELL_COLLECTION_NAME
            ).document(cell)

        return result
    }

    override suspend fun updateCell(
        church: String,
        network: String,
        subNetwork: String,
        cell: String,
        fields: Map<String, Any>
    ) {
        firebaseService.firebaseFirestore
            .collection(
                "${CHURCH_COLLECTION_NAME}/" +
                        "${church}/" +
                        "${NET_COLLECTION_NAME}/" +
                        "${network}/" +
                        "${SUBNET_COLLECTION_NAME}/" +
                        "${subNetwork}/" +
                        CELL_COLLECTION_NAME
            )
            .document(cell)
            .update(fields)
            .addOnSuccessListener { Log.i(CAPTURE_USER_FRAGMENT, "Update Success") }
            .addOnFailureListener { e ->
                Log.e(
                    CAPTURE_USER_FRAGMENT,
                    "Error: Updating document",
                    e
                )
            }
    }
}
package com.david.redcristianauno.data.remote

import com.david.redcristianauno.application.AppConstants.CELL_COLLECTION_NAME
import com.david.redcristianauno.application.AppConstants.CHURCH_COLLECTION_NAME
import com.david.redcristianauno.application.AppConstants.NET_COLLECTION_NAME
import com.david.redcristianauno.application.AppConstants.SUBNET_COLLECTION_NAME
import com.david.redcristianauno.data.network.FirebaseService
import com.david.redcristianauno.domain.models.CellDataSource
import com.david.redcristianauno.domain.models.NetworkDataSource
import com.david.redcristianauno.domain.models.SubNetworkDataSource
import com.david.redcristianauno.vo.Resource
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteChurchDataSourceImpl @Inject constructor(
    private val firebaseService: FirebaseService
) : RemoteChurchDataSource {

    override suspend fun getNetwork(church: String): Resource<List<NetworkDataSource>> {
        val result = firebaseService.firebaseFirestore.collection(
            "${CHURCH_COLLECTION_NAME}/" +
                    "${church}/" +
                    NET_COLLECTION_NAME
        )
            .get()
            .await()

        val listNet = result.toObjects(NetworkDataSource::class.java)
        return Resource.Success(listNet)
    }

    override suspend fun getSubNetwork(
        church: String,
        network: String
    ): Resource<List<SubNetworkDataSource>> {
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
        return Resource.Success(listSubNet)
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

    override suspend fun updateCell(fields: Map<String, Any>, id: String) {
        firebaseService.firebaseFirestore
    }
}
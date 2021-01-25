package com.david.redcristianauno.data.remote

import com.david.redcristianauno.application.AppConstants.CHURCH_COLLECTION_NAME
import com.david.redcristianauno.application.AppConstants.NET_COLLECTION_NAME
import com.david.redcristianauno.application.AppConstants.SUBNET_COLLECTION_NAME
import com.david.redcristianauno.data.network.FirebaseService
import com.david.redcristianauno.domain.models.CellDataSource
import com.david.redcristianauno.domain.models.NetworkDataSource
import com.david.redcristianauno.domain.models.SubNetworkDataSource
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteChurchDataSourceImpl @Inject constructor(
    private val firebaseService: FirebaseService
) : RemoteChurchDataSource{

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

    override suspend fun getSubNetwork(church: String, network: String): Resource<List<SubNetworkDataSource>> {
        val result = firebaseService.firebaseFirestore.collection(
            "${CHURCH_COLLECTION_NAME}/" +
                    "${church}/" +
                    "${NET_COLLECTION_NAME}/" +
                    "${network}/"+
                    SUBNET_COLLECTION_NAME
        )
            .get()
            .await()

        val listSubNet = result.toObjects(SubNetworkDataSource::class.java)
        return Resource.Success(listSubNet)
    }

    override suspend fun getCell(church: String, network: String, subNetwork: String): Resource<List<CellDataSource>> {
        TODO("Not yet implemented")
    }

    /*override suspend fun getAllCells(): Resource<List<ChurchDataSource>> {
        val result = firebaseService.firebaseFirestore
            .collection(CHURCH_COLLECTION_NAME)
            .get()
            .await()

        val listChurch = result.toObjects(ChurchDataSource::class.java)

        return  Resource.Success(listChurch)
    }*/
}
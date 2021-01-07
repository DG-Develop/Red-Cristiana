package com.david.redcristianauno.data.remote

import com.david.redcristianauno.application.AppConstants.CHURCH_COLLECTION_NAME
import com.david.redcristianauno.data.network.FirebaseService
import com.david.redcristianauno.domain.models.ChurchDataSource
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteChurchDataSourceImpl @Inject constructor(
    private val firebaseService: FirebaseService
) : RemoteChurchDataSource{

    override suspend fun getAllCells(): Resource<List<ChurchDataSource>> {
        val result = firebaseService.firebaseFirestore
            .collection(CHURCH_COLLECTION_NAME)
            .get()
            .await()

        val listChurch = result.toObjects(ChurchDataSource::class.java)

        return  Resource.Success(listChurch)
    }
}
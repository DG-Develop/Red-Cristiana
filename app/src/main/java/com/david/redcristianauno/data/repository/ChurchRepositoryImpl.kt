package com.david.redcristianauno.data.repository

import com.david.redcristianauno.data.remote.RemoteChurchDataSource
import com.david.redcristianauno.domain.models.ChurchDataSource
import com.david.redcristianauno.vo.Resource
import javax.inject.Inject

class ChurchRepositoryImpl @Inject constructor(
    private val remoteChurchDataSource: RemoteChurchDataSource
) : ChurchRepository{

    override suspend fun getAllCells(): Resource<List<ChurchDataSource>> =
        remoteChurchDataSource.getAllCells()
}
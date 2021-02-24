package com.david.redcristianauno.data.repository

import com.david.redcristianauno.data.remote.RemoteChurchDataSource
import com.david.redcristianauno.domain.models.CellDataSource
import com.david.redcristianauno.domain.models.NetWork
import com.david.redcristianauno.domain.models.SubNetwork
import com.david.redcristianauno.domain.models.SubNetworkDataSource
import com.david.redcristianauno.vo.Resource
import com.google.firebase.firestore.DocumentReference
import javax.inject.Inject

class ChurchRepositoryImpl @Inject constructor(
    private val remoteChurchDataSource: RemoteChurchDataSource
) : ChurchRepository {

    override suspend fun getNetwork(church: String): Resource<List<NetWork>> =
        remoteChurchDataSource.getNetwork(church)


    override suspend fun getSubNetwork(
        church: String,
        network: String
    ): Resource<List<SubNetwork>> =
        remoteChurchDataSource.getSubNetwork(church, network)

    override suspend fun getCell(
        church: String,
        network: String,
        subNetwork: String
    ): Resource<List<CellDataSource>> =
        remoteChurchDataSource.getCell(church, network, subNetwork)

    override suspend fun getPathCell(
        church: String,
        network: String,
        subNetwork: String,
        cell: String
    ): DocumentReference =
        remoteChurchDataSource.getPathCell(church, network, subNetwork, cell)

    override suspend fun updateCell(
        church: String,
        network: String,
        subNetwork: String,
        cell: String,
        fields: Map<String, Any>
    ) = remoteChurchDataSource.updateCell(church, network, subNetwork, cell, fields)
}
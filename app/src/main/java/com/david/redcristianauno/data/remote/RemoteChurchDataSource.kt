package com.david.redcristianauno.data.remote

import com.david.redcristianauno.domain.models.CellDataSource
import com.david.redcristianauno.domain.models.NetWork
import com.david.redcristianauno.domain.models.SubNetwork
import com.david.redcristianauno.vo.Resource
import com.google.firebase.firestore.DocumentReference

interface RemoteChurchDataSource {

    suspend fun getNetwork(church: String): Resource<List<NetWork>>
    suspend fun getSubNetwork(church: String, network: String): Resource<List<SubNetwork>>
    suspend fun getCell(church: String, network: String, subNetwork: String): Resource<List<CellDataSource>>
    suspend fun getPathCell(
        church: String,
        network: String,
        subNetwork: String,
        cell: String
    ): DocumentReference

    suspend fun updateCell(
        church: String,
        network: String,
        subNetwork: String,
        cell: String,
        fields: Map<String, Any>
    )
}
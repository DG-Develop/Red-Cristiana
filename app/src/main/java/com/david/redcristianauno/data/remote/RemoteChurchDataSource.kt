package com.david.redcristianauno.data.remote

import com.david.redcristianauno.domain.models.CellDataSource
import com.david.redcristianauno.domain.models.NetworkDataSource
import com.david.redcristianauno.domain.models.SubNetworkDataSource
import com.david.redcristianauno.vo.Resource

interface RemoteChurchDataSource {

    suspend fun getNetwork(church: String): Resource<List<NetworkDataSource>>
    suspend fun getSubNetwork(church: String, network: String): Resource<List<SubNetworkDataSource>>
    suspend fun getCell(church: String, network: String, subNetwork: String): Resource<List<CellDataSource>>
}
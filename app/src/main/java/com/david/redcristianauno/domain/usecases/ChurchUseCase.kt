package com.david.redcristianauno.domain.usecases

import com.david.redcristianauno.data.repository.ChurchRepository
import com.david.redcristianauno.domain.models.CellDataSource
import com.david.redcristianauno.domain.models.NetWork
import com.david.redcristianauno.domain.models.SubNetwork
import com.david.redcristianauno.domain.models.SubNetworkDataSource
import com.david.redcristianauno.vo.Resource
import com.google.firebase.firestore.DocumentReference
import javax.inject.Inject


class GetNetworkUseCase @Inject constructor(
    private val churchRepository: ChurchRepository
) {
    suspend fun invoke(church: String): Resource<List<NetWork>> =
        churchRepository.getNetwork(church)
}

class GetSubNetworkUseCase @Inject constructor(
    private val churchRepository: ChurchRepository
) {
    suspend fun invoke(church: String, network: String): Resource<List<SubNetwork>> =
        churchRepository.getSubNetwork(church, network)
}

class GetCellsUseCase @Inject constructor(
    private val churchRepository: ChurchRepository
) {
    suspend fun invoke(
        church: String,
        network: String,
        subNetwork: String
    ): Resource<List<CellDataSource>> = churchRepository.getCell(church, network, subNetwork)
}

class GePathCellUseCase @Inject constructor(
    private val churchRepository: ChurchRepository
) {
    suspend fun invoke(
        church: String,
        network: String,
        subNetwork: String,
        cell: String
    ): DocumentReference = churchRepository.getPathCell(church, network, subNetwork, cell)
}

class UpdateCellUseCase @Inject constructor(
    private val churchRepository: ChurchRepository
){
    suspend fun invoke(
        church: String,
        network: String,
        subNetwork: String,
        cell: String,
        fields: Map<String, Any>
    ) = churchRepository.updateCell(church, network, subNetwork, cell, fields)
}
package com.david.redcristianauno.domain.usecases

import com.david.redcristianauno.data.repository.ChurchRepository
import com.david.redcristianauno.domain.models.CellDataSource
import com.david.redcristianauno.domain.models.NetworkDataSource
import com.david.redcristianauno.domain.models.SubNetworkDataSource
import com.david.redcristianauno.vo.Resource
import javax.inject.Inject


class GetNetworkUseCase @Inject constructor(
    private val churchRepository: ChurchRepository
) {
    suspend fun invoke(church: String): Resource<List<NetworkDataSource>> =
        churchRepository.getNetwork(church)
}

class GetSubNetworkUseCase @Inject constructor(
    private val churchRepository: ChurchRepository
){
    suspend fun invoke(church: String, network: String) : Resource<List<SubNetworkDataSource>> =
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
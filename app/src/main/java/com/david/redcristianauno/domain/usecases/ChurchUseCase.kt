package com.david.redcristianauno.domain.usecases

import com.david.redcristianauno.data.repository.ChurchRepository
import com.david.redcristianauno.domain.models.ChurchDataSource
import com.david.redcristianauno.vo.Resource
import javax.inject.Inject

class GetCellsUseCase @Inject constructor(
    private val churchRepository: ChurchRepository
){
    suspend fun invoke(): Resource<List<ChurchDataSource>> = churchRepository.getAllCells()
}
package com.david.redcristianauno.data.repository

import com.david.redcristianauno.domain.models.ChurchDataSource
import com.david.redcristianauno.vo.Resource

interface ChurchRepository {
    suspend fun getAllCells(): Resource<List<ChurchDataSource>>
}
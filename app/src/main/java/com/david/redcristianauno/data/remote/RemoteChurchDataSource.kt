package com.david.redcristianauno.data.remote

import com.david.redcristianauno.domain.models.ChurchDataSource
import com.david.redcristianauno.vo.Resource

interface RemoteChurchDataSource {

    suspend fun getAllCells(): Resource<List<ChurchDataSource>>
}
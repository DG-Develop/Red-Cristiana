package com.david.redcristianauno.domain

import com.david.redcristianauno.data.network.HistoricalWeeklyRepository
import com.david.redcristianauno.vo.Resource

class PermissionUseCaseImpl(private val historicalDaily: HistoricalWeeklyRepository) : PermissionUseCase{
    override suspend fun getPermission(id_user: String): Resource<String> = historicalDaily.getPermission(id_user)
}
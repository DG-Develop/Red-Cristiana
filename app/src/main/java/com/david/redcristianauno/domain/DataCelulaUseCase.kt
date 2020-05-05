package com.david.redcristianauno.domain

import com.david.redcristianauno.vo.Resource


interface DataCelulaUseCase{
    suspend fun getNamesUsers(id_user: String): Resource<String>
}
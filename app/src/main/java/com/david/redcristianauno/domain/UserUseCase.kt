package com.david.redcristianauno.domain

import com.david.redcristianauno.vo.Resource


interface UserUseCase{
    suspend fun getNamesUsers(id_user: String): Resource<String>
}
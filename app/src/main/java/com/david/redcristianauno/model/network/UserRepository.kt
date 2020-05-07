package com.david.redcristianauno.model.network

import com.david.redcristianauno.vo.Resource

interface UserRepository {
    suspend fun getNamesUsers(id_user: String): Resource<String>
}
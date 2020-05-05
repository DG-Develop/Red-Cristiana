package com.david.redcristianauno.model.network

import com.david.redcristianauno.model.User
import com.david.redcristianauno.vo.Resource

interface UserRepository {
    suspend fun getUsers(): Resource<List<User>>
    suspend fun getNamesUsers(id_user: String): Resource<String>
}
package com.david.redcristianauno.data.repository

import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.vo.Resource

interface UserRepository {
    suspend fun getUserById(userId: String): Resource<User?>
}
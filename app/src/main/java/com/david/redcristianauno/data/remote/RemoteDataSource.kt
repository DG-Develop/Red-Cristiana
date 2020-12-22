package com.david.redcristianauno.data.remote

import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.vo.Resource

interface RemoteDataSource {

    suspend fun getUserById(userId: String): Resource<User?>
}
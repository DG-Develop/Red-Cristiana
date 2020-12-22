package com.david.redcristianauno.data.local

import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.vo.Resource

interface LocalDataSource {

    suspend fun getUserById(userId: String): Resource<User>?
}
package com.david.redcristianauno.data.local

import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.domain.models.UserEntity
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun getUserById(userId: String): Resource<User?>
    suspend fun getUserByIdAsFlow(userId: String): Flow<Resource<User?>>
    suspend fun createUser(data: User)
}
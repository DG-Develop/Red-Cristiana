package com.david.redcristianauno.data.remote

import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    suspend fun getUserById(userId: String): Flow<Resource<User?>>
    suspend fun loginUser(email: String, password: String): Resource<String?>
    suspend fun signOut()
    suspend fun isCurrentUser(): Boolean
    suspend fun getIdUser(): String?
}
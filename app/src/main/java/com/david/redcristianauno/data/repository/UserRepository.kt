package com.david.redcristianauno.data.repository

import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getCachedUser(userId: String): Resource<User?>
    suspend fun getUserById(userId: String): Flow<Resource<User?>>
    suspend fun loginUser(email: String, password: String): Resource<String?>
    suspend fun signOut()
    suspend fun getIdUser(): String?
    suspend fun isCurrentUser(): Boolean
    suspend fun createUserLocal(data: User)
}
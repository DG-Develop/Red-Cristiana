package com.david.redcristianauno.data.repository

import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.vo.Resource

interface UserRepository {

    suspend fun getUserByIdLocal(userId: String): Resource<User?>
    suspend fun getUserByIdRemote(userId: String): Resource<User?>
    suspend fun loginUser(email: String, password: String): Resource<String?>
    suspend fun signOut()
    suspend fun getIdUser(): String?
    suspend fun isCurrentUser(): Boolean
    suspend fun createUserLocal(data: User)
}
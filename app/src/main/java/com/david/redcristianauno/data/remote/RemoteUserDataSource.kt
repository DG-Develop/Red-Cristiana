package com.david.redcristianauno.data.remote

import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.domain.models.UserDataSource
import com.david.redcristianauno.vo.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface RemoteUserDataSource {

    suspend fun getUserById(userId: String): Flow<Resource<User?>>
    suspend fun getListUsers(filter: List<String>): Flow<Resource<List<User>>>
    suspend fun createUserAuth(email: String, password: String): Resource<AuthResult?>
    fun createUserFirestore(user: UserDataSource, callback: Callback<Void>)
    suspend fun updateUserFirestore(fields: Map<String, Any>, id: String)
    suspend fun loginUser(email: String, password: String): Resource<String?>
    suspend fun signOut()
    suspend fun isCurrentUser(): Boolean
    suspend fun getIdUser(): String?
}
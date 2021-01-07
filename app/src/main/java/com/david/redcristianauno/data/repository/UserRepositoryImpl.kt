package com.david.redcristianauno.data.repository

import android.util.Log
import com.david.redcristianauno.application.AppConstants.MAIN_ACTIVITY
import com.david.redcristianauno.data.local.LocalDataSource
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.data.remote.RemoteUserDataSource
import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.domain.models.UserDataSource
import com.david.redcristianauno.vo.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remoteUserDataSource: RemoteUserDataSource,
    private val localDataSource: LocalDataSource
) : UserRepository {

    @ExperimentalCoroutinesApi
    override suspend fun getUserById(userId: String): Flow<Resource<User?>> = callbackFlow{
        offer(getCachedUser(userId))

        remoteUserDataSource.getUserById(userId).collect { user ->
            when(user){
                is Resource.Success -> {
                    Log.i(MAIN_ACTIVITY, "Rescatando datos de manera remota")
                    user.data?.let { createUserLocal(it) }
                    offer(getCachedUser(userId))
                }
                else -> {
                    offer(getCachedUser(userId))
                }
            }
        }

        awaitClose { cancel() }
    }

    override suspend fun getListUsers(filter: List<String>): Flow<Resource<List<User>>>
    = remoteUserDataSource.getListUsers(filter)

    override suspend fun getUserByIdAsFlow(userId: String): Flow<Resource<User?>> =
        localDataSource.getUserByIdAsFlow(userId)

    override suspend fun getCachedUser(userId: String): Resource<User?> =
        localDataSource.getUserById(userId)

    override suspend fun createUserAuth(email: String, password: String): Resource<AuthResult?> =
        remoteUserDataSource.createUserAuth(email, password)

    override  fun createUserFirestore(user: UserDataSource, callback: Callback<Void>) =
        remoteUserDataSource.createUserFirestore(user, callback)

    override suspend fun updateUserFirestore(fields: Map<String, Any>, id: String) =
        remoteUserDataSource.updateUserFirestore(fields, id)


    override suspend fun loginUser(email: String, password: String): Resource<String?> =
        remoteUserDataSource.loginUser(email, password)

    override suspend fun signOut() = remoteUserDataSource.signOut()

    override suspend fun getIdUser(): String? = remoteUserDataSource.getIdUser()

    override suspend fun isCurrentUser(): Boolean = remoteUserDataSource.isCurrentUser()

    override suspend fun createUserLocal(data: User) =
        localDataSource.createUser(data)
}
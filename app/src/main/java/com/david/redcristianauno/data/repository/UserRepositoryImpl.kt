package com.david.redcristianauno.data.repository

import android.util.Log
import com.david.redcristianauno.application.AppConstants.MAIN_ACTIVITY
import com.david.redcristianauno.data.local.LocalDataSource
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.data.remote.RemoteDataSource
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
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : UserRepository {

    @ExperimentalCoroutinesApi
    override suspend fun getUserById(userId: String): Flow<Resource<User?>> = callbackFlow{
        offer(getCachedUser(userId))

        remoteDataSource.getUserById(userId).collect { user ->
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

    override suspend fun getListUsers(): Flow<Resource<List<User>>> = remoteDataSource.getListUsers()

    override suspend fun getUserByIdAsFlow(userId: String): Flow<Resource<User?>> =
        localDataSource.getUserByIdAsFlow(userId)

    override suspend fun getCachedUser(userId: String): Resource<User?> =
        localDataSource.getUserById(userId)

    override suspend fun createUserAuth(email: String, password: String): Resource<AuthResult?> =
        remoteDataSource.createUserAuth(email, password)

    override  fun createUserFirestore(user: UserDataSource, callback: Callback<Void>) =
        remoteDataSource.createUserFirestore(user, callback)

    override suspend fun loginUser(email: String, password: String): Resource<String?> =
        remoteDataSource.loginUser(email, password)

    override suspend fun signOut() = remoteDataSource.signOut()

    override suspend fun getIdUser(): String? = remoteDataSource.getIdUser()

    override suspend fun isCurrentUser(): Boolean = remoteDataSource.isCurrentUser()

    override suspend fun createUserLocal(data: User) =
        localDataSource.createUser(data)
}
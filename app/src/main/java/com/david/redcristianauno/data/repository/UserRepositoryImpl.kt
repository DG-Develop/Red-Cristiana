package com.david.redcristianauno.data.repository

import com.david.redcristianauno.data.local.LocalDataSource
import com.david.redcristianauno.data.remote.RemoteDataSource
import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.vo.Resource
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : UserRepository {

    override suspend fun getUserByIdRemote(userId: String): Resource<User?> =
        remoteDataSource.getUserById(userId)

    override suspend fun getUserByIdLocal(userId: String): Resource<User?> =
        localDataSource.getUserById(userId)

    override suspend fun loginUser(email: String, password: String): Resource<String?> =
        remoteDataSource.loginUser(email, password)

    override suspend fun signOut() = remoteDataSource.signOut()

    override suspend fun getIdUser(): String? = remoteDataSource.getIdUser()

    override suspend fun isCurrentUser(): Boolean = remoteDataSource.isCurrentUser()

    override suspend fun createUserLocal(data: User) =
        localDataSource.createUser(data)
}
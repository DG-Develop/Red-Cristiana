package com.david.redcristianauno.data.repository

import com.david.redcristianauno.data.remote.RemoteDataSource
import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.vo.Resource
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : UserRepository {

    override suspend fun getUserById(userId: String): Resource<User?> =
        remoteDataSource.getUserById(userId)
}
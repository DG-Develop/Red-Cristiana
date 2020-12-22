package com.david.redcristianauno.data.local


import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.domain.models.asUser
import com.david.redcristianauno.vo.Resource
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val userDao: UserDao
) : LocalDataSource {

    override suspend fun getUserById(userId: String): Resource<User>?{
        return userDao.getUserById(userId)?.let { Resource.Success(it.asUser()) }
    }
}
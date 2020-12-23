package com.david.redcristianauno.data.local


import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.domain.models.asUser
import com.david.redcristianauno.domain.models.asUserEntity
import com.david.redcristianauno.vo.Resource
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val userDao: UserDao
) : LocalDataSource {

    override suspend fun getUserById(userId: String): Resource<User?>{
        return Resource.Success(userDao.getUserById(userId)?.asUser())
    }

    override suspend fun createUser(data: User) {
        userDao.saveUser(data.asUserEntity())
    }
}
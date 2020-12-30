package com.david.redcristianauno.data.local


import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.domain.models.asUser
import com.david.redcristianauno.domain.models.asUserEntity
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val userDao: UserDao
) : LocalDataSource {

    override suspend fun getUserById(userId: String): Resource<User?>{
        return Resource.Success(userDao.getUserById(userId)?.asUser())
    }

    override suspend fun getUserByIdAsFlow(userId: String): Flow<Resource<User?>> = flow{
        userDao.getUserByIdAsFlow(userId).collect { userEntity->
            if (userEntity != null) {
                emit(Resource.Success(userEntity.asUser()))
            }
        }
    }

    override suspend fun createUser(data: User) {
        userDao.saveUser(data.asUserEntity())
    }
}
package com.david.redcristianauno.domain.usecases

import com.david.redcristianauno.data.repository.UserRepository
import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.vo.Resource
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend fun invoke(userId: String): Resource<User?> = userRepository.getUserById(userId)
}
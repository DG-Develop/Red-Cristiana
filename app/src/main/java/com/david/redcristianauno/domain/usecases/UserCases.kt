package com.david.redcristianauno.domain.usecases

import com.david.redcristianauno.data.repository.UserRepository
import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.vo.Resource
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend fun invokeRemote(userId: String): Resource<User?> = userRepository.getUserByIdRemote(userId)
    suspend fun invokeLocal(userId: String): Resource<User?> = userRepository.getUserByIdLocal(userId)
}

class LoginUserUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend fun invoke(email: String, password: String) = userRepository.loginUser(email, password)
}

class CurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend fun invoke() = userRepository.isCurrentUser()
}

class CreateUserUseCase @Inject constructor(
    private val  userRepository: UserRepository
){
    suspend fun invokeLocal(data: User) = userRepository.createUserLocal(data)
}

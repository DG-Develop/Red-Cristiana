package com.david.redcristianauno.domain.usecases

import com.david.redcristianauno.data.repository.UserRepository
import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend fun invoke(userId: String): Flow<Resource<User?>> = userRepository.getUserById(userId)
}

class GetListUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend fun invoke(): Flow<Resource<List<User>>> = userRepository.getListUsers()
}

class GetUserByIdUseCaseAsFlow @Inject constructor(
    private val userRepository: UserRepository
){
    suspend fun invoke(userId: String): Flow<Resource<User?>> = userRepository.getUserByIdAsFlow(userId)
}

class GetUserCached @Inject constructor(
    private val userRepository: UserRepository
){
    suspend fun invoke(userId: String): Resource<User?> = userRepository.getCachedUser(userId)
}

class LoginUserUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend fun invoke(email: String, password: String) = userRepository.loginUser(email, password)
}

class SignOutUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend fun invoke() = userRepository.signOut()
}

class GetIdUserUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend fun invoke() = userRepository.getIdUser()
}

class CurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend fun invoke() = userRepository.isCurrentUser()
}

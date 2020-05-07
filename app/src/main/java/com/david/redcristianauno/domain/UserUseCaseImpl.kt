package com.david.redcristianauno.domain

import com.david.redcristianauno.model.network.UserRepository
import com.david.redcristianauno.vo.Resource

class UserUseCaseImpl(private val userRepo: UserRepository): UserUseCase {
    override suspend fun getNamesUsers(id_user: String): Resource<String> = userRepo.getNamesUsers(id_user)
}
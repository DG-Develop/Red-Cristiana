package com.david.redcristianauno.domain

import com.david.redcristianauno.data.model.User
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.data.network.UserRepository

class ProfileUseCaseImpl(private val userRepo: UserRepository) : ProfileUseCase{
    override fun getDataUser(callback: Callback<User>) = userRepo.getDataUser(callback)
    override fun updateDataUser(names: String, last_names: String, telephone: String, address: String) = userRepo.updateDataUser(names, last_names, telephone, address)
}
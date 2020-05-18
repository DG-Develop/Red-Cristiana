package com.david.redcristianauno.domain

import android.content.Context
import android.widget.Spinner
import com.david.redcristianauno.data.network.UserRepository
import com.david.redcristianauno.vo.Resource

class UserUseCaseImpl(private val userRepo: UserRepository): UserUseCase {
    override suspend fun getNamesUsers(id_user: String): Resource<String> = userRepo.getNamesUsers(id_user)
    override fun getSubredesForFillSpinner(context: Context, spinner: Spinner)  = userRepo.getSubredesForFillSpinner(context, spinner)
    override fun getCelulasForFillSpinner(context: Context, spinner: Spinner, subred: String) = userRepo.getCelulasForFillSpinner(context, spinner, subred)
}
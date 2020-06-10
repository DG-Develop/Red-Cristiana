package com.david.redcristianauno.domain

import android.content.Context
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.data.network.UserRepository
import com.david.redcristianauno.vo.Resource

class UserUseCaseImpl(private val userRepo: UserRepository): UserUseCase {
    override suspend fun getNamesUsers(id_user: String): Resource<String> = userRepo.getNamesUsers(id_user)
    override fun getCelulasForFillTil(subred: String, callback: Callback<MutableList<String>>)
    = userRepo.getCelulasForFillTil(subred, callback)
    override fun getSubredesForFillTil(callback: Callback<MutableList<String>>)
    = userRepo.getSubredesForFillTil(callback)
}
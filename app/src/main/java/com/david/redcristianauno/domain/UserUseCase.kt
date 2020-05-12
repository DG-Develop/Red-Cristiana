package com.david.redcristianauno.domain

import android.content.Context
import android.widget.Spinner
import com.david.redcristianauno.vo.Resource


interface UserUseCase{
    suspend fun getNamesUsers(id_user: String): Resource<String>
    fun getSubredesForFillSpinner(context: Context, spinner: Spinner)
    fun getCelulasForFillSpinner(context: Context,spinner: Spinner, subred: String)
}
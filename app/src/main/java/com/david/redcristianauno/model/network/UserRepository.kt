package com.david.redcristianauno.model.network

import android.content.Context
import android.widget.Spinner
import com.david.redcristianauno.model.User
import com.david.redcristianauno.vo.Resource

interface UserRepository {
    suspend fun getNamesUsers(id_user: String): Resource<String>
    fun getSubredesForFillSpinner(context: Context,spinner: Spinner)
    fun getCelulasForFillSpinner(context: Context,spinner: Spinner, subred: String)
    fun getDataUser(callback: Callback<User>)
    fun updateDataUser(names: String, last_names: String, telephone: String, address: String)
}
package com.david.redcristianauno.domain

import com.david.redcristianauno.data.model.User
import com.david.redcristianauno.data.network.Callback

interface ProfileUseCase {
    fun getDataUser(callback: Callback<User>)
    fun updateDataUser(names: String, last_names: String, telephone: String, address: String)
}
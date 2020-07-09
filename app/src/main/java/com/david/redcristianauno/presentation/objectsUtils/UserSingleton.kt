package com.david.redcristianauno.presentation.objectsUtils

import com.david.redcristianauno.data.model.User


object UserSingleton {
    private var userLogin: User? = null

    fun setUser(user: User){
        this.userLogin = user
    }

    fun getUser(): User? = userLogin
}
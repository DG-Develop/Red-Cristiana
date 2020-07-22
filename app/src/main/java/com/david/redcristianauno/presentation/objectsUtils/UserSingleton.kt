package com.david.redcristianauno.presentation.objectsUtils

import com.david.redcristianauno.data.model.User
import com.google.firebase.firestore.DocumentReference


object UserSingleton {
    private var userLogin: User? = null

    fun setUser(user: User){
        this.userLogin = user
    }

    fun getUser(): User? = userLogin

    fun updateChurch(church: DocumentReference){
        userLogin?.iglesia_references = church
    }
}
package com.david.redcristianauno.presentation.objectsUtils

import com.david.redcristianauno.data.model.User
import com.google.firebase.firestore.DocumentReference

object UserSingleton {
    private var userLogin: User? = null

    fun setUser(user: User){
        this.userLogin = user
    }

    fun getUser(): User? = userLogin

    fun getIdEntity(type: String): String? {
        val documents = userLogin?.iglesia_references?.path?.split("/")
        return when(type){
            "Iglesia" -> documents?.get(1)
            "Red" -> documents?.get(3)
            "Subred" -> documents?.get(5)
            "Celula" -> documents?.get(7)
            else -> "No existe este campo"
        }
    }
    fun updateChurch(church: DocumentReference){
        userLogin?.iglesia_references = church
    }
}
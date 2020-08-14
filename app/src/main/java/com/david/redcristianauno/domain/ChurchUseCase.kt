package com.david.redcristianauno.domain

import com.david.redcristianauno.data.model.*
import com.david.redcristianauno.data.network.Callback
import com.google.firebase.firestore.DocumentReference

interface ChurchUseCase {
    fun getIglesias(callback: Callback<MutableList<Iglesia>>)
    fun getRedes(name: String, callback: Callback<MutableList<String>>)
    fun getSubredes(iglesia: String, red: String, callback: Callback<MutableList<String>>)
    fun getCelulas(iglesia: String, red: String, subred: String, callback: Callback<MutableList<String>>)
    fun updateDataChurch(iglesia_references: DocumentReference)
    fun getRedObject(name: String, callback: Callback<MutableList<Red>>)
    fun getSubredObject(iglesia: String, red: String, callback: Callback<List<Subred>>)
    fun getCelulaObject(iglesia: String, red: String, subred: String, callback: Callback<MutableList<Celula>>)
    fun getListUser(callback: Callback<List<User>>)
    fun searchUserWithoutSomeParams(char: String, key: String, callback: Callback<List<User>>)
}
package com.david.redcristianauno.data.network

import com.david.redcristianauno.data.model.*


interface ChurchRepository {
    fun getIglesias(callback: Callback<MutableList<Iglesia>>)
    fun getRedes(name: String, callback: Callback<MutableList<String>>)
    fun getSubredes(iglesia: String, red: String, callback: Callback<MutableList<String>>)
    fun getCelulas(iglesia: String, red: String, subred: String, callback: Callback<MutableList<String>>)
    fun getRedObject(name: String, callback: Callback<MutableList<Red>>)
    fun getSubredObject(iglesia: String, red: String, callback: Callback<List<Subred>>)
    fun getCelulaObject(iglesia: String, red: String, subred: String, callback: Callback<MutableList<Celula>>)
}
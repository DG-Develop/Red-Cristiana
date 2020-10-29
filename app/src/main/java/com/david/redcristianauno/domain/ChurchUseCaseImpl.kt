package com.david.redcristianauno.domain

import com.david.redcristianauno.data.model.*
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.data.network.ChurchRepository
import com.david.redcristianauno.data.network.UserRepository
import com.google.firebase.firestore.DocumentReference

class ChurchUseCaseImpl(
    private val churchRepository: ChurchRepository,
    private val userRepository: UserRepository
) : ChurchUseCase {

    override fun getIglesias(callback: Callback<MutableList<Iglesia>>) =
        churchRepository.getIglesias(callback)

    override fun getRedes(name: String, callback: Callback<MutableList<String>>) =
        churchRepository.getRedes(name, callback)

    override fun getSubredes(
        iglesia: String, red: String, callback: Callback<MutableList<String>>
    ) = churchRepository.getSubredes(iglesia, red, callback)

    override fun getCelulas(
        iglesia: String, red: String, subred: String, callback: Callback<MutableList<String>>
    ) = churchRepository.getCelulas(iglesia, red, subred, callback)

    override fun updateDataChurch(iglesia_references: DocumentReference) =
        userRepository.updateDataChurch(iglesia_references)

    override fun getRedObject(name: String, callback: Callback<MutableList<Red>>) =
        churchRepository.getRedObject(name, callback)

    override fun getSubredObject(
        iglesia: String,
        red: String,
        callback: Callback<List<Subred>>
    ) = churchRepository.getSubredObject(iglesia, red, callback)

    override fun getCelulaObject(
        iglesia: String,
        red: String,
        subred: String,
        callback: Callback<MutableList<Celula>>
    ) = churchRepository.getCelulaObject(iglesia, red, subred, callback)

    override fun getListUser(callback: Callback<List<User>>) = userRepository.getListUser(callback)

    override fun filterByPermission(permission: String, callback: Callback<List<User>>) =
        userRepository.filterByPermission(permission, callback)

    override fun searchUserWithoutSomeParams(
        permission: String, char: String, key: String, callback: Callback<List<User>>
    ) = userRepository.searchUserWithoutSomeParams(permission, char, key, callback)
}
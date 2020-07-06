package com.david.redcristianauno.domain

import com.david.redcristianauno.data.model.Iglesia
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.data.network.ChurchRepository

class ChurchUseCaseImpl(private val churchRepository: ChurchRepository) : ChurchUseCase {

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
}
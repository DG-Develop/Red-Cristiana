package com.david.redcristianauno.data.model

data class CreateEntityModel(
    var name: String,
    var email: String = "",
    var image: String,
    var id: String = ""
)

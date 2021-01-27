package com.david.redcristianauno.domain.models

import com.google.firebase.firestore.DocumentReference

data class ChurchDataSource(
    var created_by: DocumentReference? = null,
    var id_iglesia: String = "",
    var name: String = ""
)

data class NetworkDataSource(
    var created_by: DocumentReference? = null,
    var id_red: String = "",
    var name_leader: String = ""
)

data class SubNetworkDataSource(
    var created_by: DocumentReference? = null,
    var id_subred: String = "",
    var name_leader: String = "",
    var max_celula: Int = 0
)

data class CellDataSource(
    var created_by: DocumentReference? = null,
    var id_celula: String = "",
    var name_leader: String = "",
    var users: List<DocumentReference> = listOf()
)
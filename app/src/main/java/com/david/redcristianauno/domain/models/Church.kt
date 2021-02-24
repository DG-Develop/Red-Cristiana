package com.david.redcristianauno.domain.models

import com.david.redcristianauno.data.model.GeneralModel
import com.google.firebase.firestore.DocumentReference
import java.io.Serializable

data class ChurchDataSource(
    var created_by: DocumentReference? = null,
    var id_iglesia: String = "",
    var name: String = ""
)

data class NetworkDataSource(
    var created_by: DocumentReference? = null,
    var id_red: String = "",
    var leader: DocumentReference? = null
)

data class NetWork(
    val id_red: String = "",
    val created_by: UserDataSource,
    val leader: UserDataSource
): Serializable

data class SubNetworkDataSource(
    var created_by: DocumentReference? = null,
    var id_subred: String = "",
    var leader: DocumentReference? = null,
    var max_celula: Int = 0
)

data class SubNetwork(
    val id_subred: String = "",
    val created_by: UserDataSource,
    val leader: UserDataSource,
    var max_celula: Int = 0
)

data class CellDataSource(
    var created_by: DocumentReference? = null,
    var id_celula: String = "",
    var leader: DocumentReference? = null,
    var users: MutableList<DocumentReference> = mutableListOf()
)

fun List<NetWork>.asListGeneralModel(): List<GeneralModel> = this.map {
    GeneralModel(
        it.id_red, it.leader.names, it.leader.permission
    )
}

fun List<SubNetwork>.asListGeneralModelSub(): List<GeneralModel> = this.map {
    GeneralModel(
        it.id_subred, it.leader.names, it.leader.permission
    )
}
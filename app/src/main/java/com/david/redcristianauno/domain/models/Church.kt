package com.david.redcristianauno.domain.models

import android.os.Parcelable
import com.david.redcristianauno.data.model.GeneralModel
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.parcel.Parcelize

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

@Parcelize
data class NetWork(
    val id_red: String = "",
    val created_by: User,
    val leader: User
): Parcelable

data class SubNetworkDataSource(
    var created_by: DocumentReference? = null,
    var id_subred: String = "",
    var leader: DocumentReference? = null,
    var max_celula: Int = 0
)

data class CellDataSource(
    var created_by: DocumentReference? = null,
    var id_celula: String = "",
    var leader: DocumentReference? = null,
    var users: MutableList<DocumentReference> = mutableListOf()
)

fun List<NetworkDataSource>.asListGeneralModel(): List<GeneralModel> = this.map {
    GeneralModel(
        it.id_red, it.leader!!.path
    )
}
package com.david.redcristianauno.domain.models

import com.david.redcristianauno.application.AppConstants
import com.david.redcristianauno.data.model.GeneralModel
import com.david.redcristianauno.data.network.FirebaseService
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
): Serializable

data class CellDataSource(
    var created_by: DocumentReference? = null,
    var id_celula: String = "",
    var leader: DocumentReference? = null,
    var users: MutableList<DocumentReference> = mutableListOf()
)

data class Cell(
    var id_celula: String = "",
    var created_by: UserDataSource,
    var leader: UserDataSource,
    var users: MutableList<UserDataSource?> = mutableListOf()
): Serializable

fun List<Cell>.asListCellDataSource(): List<CellDataSource> = this.map {
    val firebaseService = FirebaseService()
    val created_by: DocumentReference = firebaseService.firebaseFirestore.collection(
        AppConstants.USER_COLLECTION_NAME
    ).document(it.created_by.id)

    val leader: DocumentReference = firebaseService.firebaseFirestore.collection(
        AppConstants.USER_COLLECTION_NAME
    ).document(it.leader.id)

    val users = it.users.map { user->
       firebaseService.firebaseFirestore.collection(
            AppConstants.USER_COLLECTION_NAME
        ).document(user!!.id)
    }

    CellDataSource(created_by, it.id_celula, leader, users.toMutableList())
}

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

fun List<Cell>.asListGeneralModelCell(): List<GeneralModel> = this.map {
    GeneralModel(
        it.id_celula, it.leader.names, it.leader.permission
    )
}
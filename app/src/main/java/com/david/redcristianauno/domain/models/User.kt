package com.david.redcristianauno.domain.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.parcel.Parcelize


data class UserDataSource(
    val id: String = "",
    val names: String = "",
    val last_names: String = "",
    val email: String = "",
    val address: String = "",
    val telephone: String = "",
    val permission: List<String> = listOf(),
    val subred_name: String = "",
    val iglesia_references: DocumentReference? = null
)

@Parcelize
data class User(
    val id: String = "",
    val names: String = "",
    val last_names: String = "",
    val email: String = "",
    val address: String = "",
    val telephone: String = "",
    val permission: List<String> = listOf(),
    val subred_name: String = "",
    val iglesia_references: String? = null
) : Parcelable

@Entity(tableName = "userTable")
data class UserEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "user_name")
    val names: String,
    @ColumnInfo(name = "user_lastNames")
    val last_names: String,
    @ColumnInfo(name = "user_email")
    val email: String,
    @ColumnInfo(name = "user_address")
    val address: String,
    @ColumnInfo(name = "user_telephone")
    val telephone: String,
    @ColumnInfo(name = "user_permission")
    val permission: List<String>,
    @ColumnInfo(name = "user_subredName")
    val subred_name: String,
    @ColumnInfo(name = "user_iglesiaReferences")
    val iglesia_references: String?
)

fun UserEntity.asUser() = User(
    id,
    names,
    last_names,
    email,
    address,
    telephone,
    permission,
    subred_name,
    iglesia_references
)


fun User.asUserEntity() = UserEntity(
    id,
    names,
    last_names,
    email,
    address,
    telephone,
    permission,
    subred_name,
    iglesia_references
)

fun UserDataSource.asUser() = User(
    id, names, last_names, email, address, telephone, permission, subred_name,
    iglesia_references?.path
)

fun List<UserDataSource>.asListUser(): List<User> = this.map {
    User(
        it.id,
        it.names,
        it.last_names,
        it.email,
        it.address,
        it.telephone,
        it.permission,
        it.subred_name,
        it.iglesia_references?.path
    )
}
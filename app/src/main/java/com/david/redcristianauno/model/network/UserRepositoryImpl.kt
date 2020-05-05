package com.david.redcristianauno.model.network

import com.david.redcristianauno.model.User
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl: UserRepository{
    val firebaseService = FirebaseService()

    override suspend fun getUsers(): Resource<List<User>> {
        val resultData = firebaseService.firebaseFirestore
            .collection("users")
            .document()
            .get()
            .await()

        val listUser: List<User>? = null //resultData.toObject(User::class.java)

        return  Resource.Success(listUser!!)
    }

    override suspend fun getNamesUsers(id_user: String): Resource<String> {
        val resultData = firebaseService.firebaseFirestore
            .collection("users")
            .document(id_user)
            .get()
            .await()

        val nameUser = resultData.getString("names")

        return Resource.Success(nameUser!!)
    }

}
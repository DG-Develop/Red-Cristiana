package com.david.redcristianauno.model.network

import com.david.redcristianauno.model.network.FirebaseService.Companion.USER_COLLECTION_NAME
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl: UserRepository{
    val firebaseService = FirebaseService()

    override suspend fun getNamesUsers(id_user: String): Resource<String> {
        val resultData = firebaseService.firebaseFirestore
            .collection(USER_COLLECTION_NAME)
            .document(id_user)
            .get()
            .await()

        val nameUser = resultData.getString("names")

        return Resource.Success(nameUser!!)
    }

}
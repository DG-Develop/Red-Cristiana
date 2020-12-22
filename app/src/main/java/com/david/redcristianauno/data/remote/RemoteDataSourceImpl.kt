package com.david.redcristianauno.data.remote

import com.david.redcristianauno.data.network.FirebaseService
import com.david.redcristianauno.data.network.FirebaseService.Companion.USER_COLLECTION_NAME
import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.tasks.await

class RemoteDataSourceImpl(): RemoteDataSource {
    private val firebaseService: FirebaseService = FirebaseService()


    override suspend fun getUserById(userId: String): Resource<User?> {
        val resultData = firebaseService.firebaseFirestore.
            collection(USER_COLLECTION_NAME)
            .document(userId)
            .get()
            .await()

        return Resource.Success(resultData.toObject(User::class.java))
    }
}
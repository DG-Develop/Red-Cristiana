package com.david.redcristianauno.data.remote

import com.david.redcristianauno.application.AppConstants.USER_COLLECTION_NAME
import com.david.redcristianauno.data.network.FirebaseService
import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.domain.models.UserDataSource
import com.david.redcristianauno.domain.models.asUser
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val firebaseService: FirebaseService): RemoteDataSource {

    override suspend fun getUserById(userId: String): Resource<User?> {
        val resultData = firebaseService.firebaseFirestore.
            collection(USER_COLLECTION_NAME)
            .document(userId)
            .get()
            .await()

        return Resource.Success(resultData.toObject(UserDataSource::class.java)?.asUser())
    }

    override suspend fun loginUser(email: String, password: String): Resource<String?> {
        val result = firebaseService.firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return  Resource.Success(result.user?.uid)
    }

    override suspend fun signOut() = firebaseService.firebaseAuth.signOut()

    override suspend fun getIdUser(): String? = firebaseService.firebaseAuth.uid

    override suspend fun isCurrentUser(): Boolean = firebaseService.firebaseAuth.currentUser != null

}
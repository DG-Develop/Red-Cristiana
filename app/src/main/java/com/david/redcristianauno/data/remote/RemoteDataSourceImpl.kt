package com.david.redcristianauno.data.remote

import android.util.Log
import com.david.redcristianauno.application.AppConstants.PROFILE_FRAGMENT
import com.david.redcristianauno.application.AppConstants.REGISTER_ACTIVITY
import com.david.redcristianauno.application.AppConstants.USER_COLLECTION_NAME
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.data.network.FirebaseService
import com.david.redcristianauno.domain.models.*
import com.david.redcristianauno.vo.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val firebaseService: FirebaseService
) : RemoteDataSource {

    @ExperimentalCoroutinesApi
    override suspend fun getUserById(userId: String): Flow<Resource<User?>> = callbackFlow {
        val evenDocument = firebaseService.firebaseFirestore
            .collection(USER_COLLECTION_NAME)
            .document(userId)

        val subscription =
            evenDocument.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                if (documentSnapshot != null) {
                    if (documentSnapshot.exists()) {
                        val user = documentSnapshot.toObject(UserDataSource::class.java)
                        offer(Resource.Success(user?.asUser()))
                    } else {
                        channel.close(firebaseFirestoreException?.cause)
                    }
                }
            }

        awaitClose { subscription.remove() }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getListUsers(): Flow<Resource<List<User>>> = callbackFlow {
        val eventsDocuments = firebaseService.firebaseFirestore
            .collection(USER_COLLECTION_NAME)
            .whereArrayContainsAny("permission", listOf("Normal", "Lider Celula", "Subred", "Red"))

        val subscription =
            eventsDocuments.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                if (documentSnapshot != null) {
                    val users = documentSnapshot
                        .toObjects(UserDataSource::class.java)
                        .asListUser()
                        .sortedBy { it.names }
                    offer(Resource.Success(users))
                } else {
                    channel.close(firebaseFirestoreException?.cause)
                }

            }

        awaitClose { subscription.remove() }
    }

    override suspend fun createUserAuth(email: String, password: String): Resource<AuthResult?> {
        val result = firebaseService.firebaseAuth
            .createUserWithEmailAndPassword(email, password)
            .await()

        return Resource.Success(result)
    }

    override fun createUserFirestore(user: UserDataSource, callback: Callback<Void>) {
        firebaseService.firebaseFirestore
            .collection(USER_COLLECTION_NAME)
            .document(user.id)
            .set(user)
            .addOnSuccessListener {
                Log.i(REGISTER_ACTIVITY, "Creado con exito")
                callback.OnSucces(null)
            }
            .addOnFailureListener{exception ->
                Log.e(REGISTER_ACTIVITY, "Error al crear el archivo: ${exception.message}")
                callback.onFailure(exception )
            }
    }

    override suspend fun updateUserFirestore(fields: Map<String, String>) {
        getIdUser()?.let { id->
            firebaseService.firebaseFirestore
                .collection(USER_COLLECTION_NAME)
                .document(id)
                .update(fields)
                .addOnSuccessListener { Log.i(PROFILE_FRAGMENT, "Update Success") }
                .addOnFailureListener{ e-> Log.e(PROFILE_FRAGMENT, "Error: Updating document", e)}
        }
    }

    override suspend fun loginUser(email: String, password: String): Resource<String?> {
        val result =
            firebaseService.firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return Resource.Success(result.user?.uid)
    }

    override suspend fun signOut() = firebaseService.firebaseAuth.signOut()

    override suspend fun getIdUser(): String? = firebaseService.firebaseAuth.uid

    override suspend fun isCurrentUser(): Boolean = firebaseService.firebaseAuth.currentUser != null

}
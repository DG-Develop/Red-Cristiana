package com.david.redcristianauno.model.network

import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.david.redcristianauno.model.News

class NoticeRepositoryImpl : NoticeRepository {
    val firebaseService = FirebaseService()

    override fun uploadNews(uri: Uri, title: String, description: String, photo: ByteArray, progress: ProgressBar, view: View) {
        val filePath = firebaseService.mStorage.child("News").child(uri.lastPathSegment!!)
        val uploadTask = filePath.putBytes(photo)
            .addOnSuccessListener {
                Log.i("NewsInfo", "Image upload success")
            }
            .addOnCanceledListener {
                Log.i("NewsInfo", "Failed upload Image")
            }
        uploadTask
            .addOnProgressListener { task ->
                progress.progress = (100*task.bytesTransferred/task.totalByteCount).toInt()
            }
            .continueWithTask{task ->
                if (task.isSuccessful){
                    task.exception?.let{
                        throw  it
                    }
                }
                filePath.downloadUrl
            }
            .addOnCompleteListener{task ->
            if (task.isSuccessful){
               val news =  News()
                news.title = title
                news.pathPhoto = task.result.toString()
                news.description = description
                news.id_user = firebaseService.firebaseAuth.currentUser?.uid!!
                news.email_user = firebaseService.firebaseAuth.currentUser?.email!!

                firebaseService.firebaseFirestore.collection("news").document().set(news)
                    .addOnCompleteListener{
                        view.visibility = View.GONE
                        Log.i("NewsInfo", "Image upload in database")
                    }
            }
        }
    }
}
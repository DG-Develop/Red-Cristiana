package com.david.redcristianauno.viewmodel

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.lifecycle.ViewModel
import com.david.redcristianauno.domain.NoticeUseCase
import java.io.ByteArrayOutputStream

class NoticeViewModel(news: NoticeUseCase): ViewModel() {
    private val notice = news

    fun compressImage(uri: Uri, title: String, description: String, img: ImageView, progress: ProgressBar, view: View){
        val imgBtm = img.drawable as BitmapDrawable
        val imgCompress = imgBtm.bitmap

        val byteArrayOutputStream = ByteArrayOutputStream()
        imgCompress.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)

        val photo = byteArrayOutputStream.toByteArray()

        uploadNotice(uri, title, description, photo, progress, view)
    }

     private fun uploadNotice(uri: Uri, title: String, description: String, photo: ByteArray, progress: ProgressBar, view: View){
         notice.uploadNews(uri, title, description, photo, progress, view)
     }
}
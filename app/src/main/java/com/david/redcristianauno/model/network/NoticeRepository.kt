package com.david.redcristianauno.model.network

import android.net.Uri
import android.view.View
import android.widget.ProgressBar

interface NoticeRepository {
    fun uploadNews(uri: Uri, title: String, description: String, photo: ByteArray, progress: ProgressBar, view: View)
}
package com.david.redcristianauno.domain

import android.net.Uri
import android.view.View
import android.widget.ProgressBar

interface NoticeUseCase {
    fun uploadNews(uri: Uri, title: String, description: String, photo: ByteArray, progress: ProgressBar, view: View)
}
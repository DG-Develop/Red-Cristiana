package com.david.redcristianauno.domain

import android.net.Uri
import android.view.View
import android.widget.ProgressBar
import com.david.redcristianauno.model.News
import com.david.redcristianauno.model.network.Callback

interface NoticeUseCase {
    fun uploadNews(uri: Uri, title: String, description: String, photo: ByteArray, progress: ProgressBar, view: View)
    fun getNews(callback: Callback<List<News>>)
}
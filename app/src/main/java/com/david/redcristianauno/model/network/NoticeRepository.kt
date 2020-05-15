package com.david.redcristianauno.model.network

import android.net.Uri
import android.view.View
import android.widget.ProgressBar
import com.david.redcristianauno.model.News

interface NoticeRepository {
    fun uploadNews(uri: Uri, title: String, description: String, photo: ByteArray, progress: ProgressBar, view: View)
    fun getNews(callback: Callback<List<News>>)
}
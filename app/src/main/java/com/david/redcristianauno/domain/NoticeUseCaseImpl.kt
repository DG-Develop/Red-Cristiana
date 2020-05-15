package com.david.redcristianauno.domain

import android.net.Uri
import android.view.View
import android.widget.ProgressBar
import com.david.redcristianauno.model.News
import com.david.redcristianauno.model.network.Callback
import com.david.redcristianauno.model.network.NoticeRepository

class NoticeUseCaseImpl(private val news: NoticeRepository): NoticeUseCase {
    override fun uploadNews(uri: Uri, title: String, description: String, photo: ByteArray, progress: ProgressBar, view: View) = news.uploadNews(uri, title, description, photo, progress, view)
    override fun getNews(callback: Callback<List<News>>) = news.getNews(callback)
}
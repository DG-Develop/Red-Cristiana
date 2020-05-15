package com.david.redcristianauno.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.david.redcristianauno.domain.NoticeUseCase
import com.david.redcristianauno.model.News
import com.david.redcristianauno.model.network.Callback
import java.lang.Exception

class HomeViewModel(newsUseCase: NoticeUseCase) : ViewModel(){
    private val news = newsUseCase
    var listNews: MutableLiveData<List<News>> = MutableLiveData()
    var isLoading = MutableLiveData<Boolean>()

    fun refresh(){
        getNewsFromFirebase()
    }

    private fun getNewsFromFirebase(){
        news.getNews(object : Callback<List<News>>{
            override fun OnSucces(result: List<News>?) {
                listNews.postValue(result)
                processFinished()
                if (result.isNullOrEmpty()){
                    processFinished()
                }
            }

            override fun onFailure(exception: Exception) {

            }
        })
    }

    private fun processFinished() {
        isLoading.value = true
    }

}